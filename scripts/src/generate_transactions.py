# Script to generate sample transactions
import pandas as pd
import random
from datetime import datetime, timedelta

# If PyYaml is not installed, config_path will be ignored. This should not occur if requirements.txt is used.
try:
    import yaml
except Exception as e:
    print(e)
    yaml = None


class TransactionGenerator:
    def __init__(
        self,
        num_transactions: int = 1,
        max_amount: float = 1000.0,
        start = None,
        end = None,
        path:str = "scripts/data/transactions.csv",
        details_path: str = "scripts/data/transaction_details.csv",
        config_path: str | None = None,
    ):
        """
        - num_transactions: number of rows to generate (default 1).
        - start, end: datetime . If None, defaults to last 30 days.
        - path: output CSV file path.
        - config_path: optional YAML file path to override defaults (keys: num_transactions, start, end, path).
        """
        # Allow YAML to override defaults if provided
        if config_path and yaml:
            try:
                with open(config_path, "r", encoding="utf-8") as f:
                    cfg = yaml.safe_load(f) or {}
                num_transactions = cfg.get("num_transactions", num_transactions)
                max_amount = cfg.get("max_amount", max_amount)
                start = cfg.get("start", start)
                end = cfg.get("end", end)
                path = cfg.get("path", path)
                details_path = cfg.get("details_path", details_path)
                print(f"Loaded items using config from {config_path}")
            except FileNotFoundError:
                pass

        self.num_transactions: int = int(num_transactions)
        self.max_amount: float = float(max_amount)

        # Default to the last 30 days if start/end not provided
        if start is None or end is None:
            self.end_timestamp = datetime.now()
            self.start_timestamp = self.end_timestamp - timedelta(days=30)
        else: # Use provided start/end
            self.start_timestamp = datetime(start.year, start.month, start.day, 0, 0, 0)
            self.end_timestamp = datetime(end.year, end.month, end.day, 23, 59, 59)

        # Ensure ordering
        if self.end_timestamp < self.start_timestamp:
            self.start_timestamp, self.end_timestamp = self.end_timestamp, self.start_timestamp

        self.path = path
        self.details_path = details_path
        self.config_path = config_path
        self.details: pd.DataFrame = pd.DataFrame(columns=["item_id", "temp_id"])

    def random_date(self, start: datetime, end: datetime) -> datetime:
        """Return a random datetime between start and end."""
        # Ensure both datetimes are naive (no timezone info)
        if start.tzinfo is not None or end.tzinfo is not None:
            raise ValueError("start and end must be naive datetimes")
        
        # Ensure start is before end
        if end < start:
            start, end = end, start
        
        start_ts = start.timestamp()
        end_ts = end.timestamp()
        ts = random.uniform(start_ts, end_ts)
        return datetime.fromtimestamp(ts).replace(tzinfo=None)

    def random_name(self) -> str:
        """Return a random full name. If names.yaml exists, use it; otherwise use hardcoded names."""
        if yaml:
            try:
                with open("data/transactions.yaml", "r", encoding="utf-8") as f:
                    names_cfg = yaml.safe_load(f) or {}
                first_names = names_cfg.get("first_names", [])
                last_names = names_cfg.get("last_names", [])
                if first_names and last_names:
                    return f"{random.choice(first_names)} {random.choice(last_names)}"
            except FileNotFoundError:
                pass

        # Fallback if no YAML or file not found
        first_names = ["John", "Jane", "Alice", "Bob", "Charlie", "Diana"]
        last_names = ["Smith", "Doe", "Johnson", "Brown", "Davis", "Miller"]
        return f"{random.choice(first_names)} {random.choice(last_names)}"
    
    def random_menu_item(self, menu: pd.DataFrame, num: int) -> pd.DataFrame:
        """Return a random selection of num menu items from the menu DataFrame."""
        return menu.sample(n = num, replace = True).reset_index(drop = True)

    def generateTransactions(self) -> pd.DataFrame:
        """Generate transactions and save to CSV. Returns the DataFrame."""
         # Prepare to collect rows
         # Load personnel and menu data
         # For each transaction:
         #   - Generate random customer name
         #   - Generate random transaction time
         #   - Select random employee_id from personnel
         #   - Select random menu items and calculate total price
         #   - Append to rows list
         # Save transactions and details to CSV files
        rows = []
        total = 0.0
        try:
            personnel: pd.DataFrame = pd.read_csv("scripts/data/personnel.csv")
            menu: pd.DataFrame = pd.read_csv("scripts/data/menu.csv")
        except FileNotFoundError as e:
            print(f"Error: {e}")
            return pd.DataFrame(columns=['customer_name', 'transaction_time', 'employee_id', 'total_price'])

        employee_ids = personnel['id'].tolist()

        for i in range(self.num_transactions):
            # Stop if total exceeds max_amount
            if total >= self.max_amount:
                break

            customer_name = self.random_name()
            transaction_time = self.random_date(self.start_timestamp, self.end_timestamp)  # naive datetime
            if transaction_time.tzinfo is not None:
                transaction_time = transaction_time.replace(tzinfo=None)
            
            employee_id = random.choice(employee_ids)
            order = self.random_menu_item(menu, random.randint(1, 5))
            total_price = round(order['price'].sum(), 2)
            total += total_price

            # Record details and store in self.details
            for _, item in order.iterrows():
                self.details = pd.concat([self.details, pd.DataFrame([{
                    'item_id': item['item_id'],
                    'temp_id': i  # Temporary ID to join with transactions
                }])], ignore_index=True)

            rows.append({
                'customer_name': customer_name,
                'transaction_time': transaction_time,  # naive datetime for TIMESTAMP WITHOUT TIME ZONE
                'employee_id': employee_id,
                'total_price': total_price,
                'temp_id': i  # Temporary ID for joining with details
            })
        # Save to CSV
        output = pd.DataFrame(rows, columns=['customer_name', 'transaction_time', 'employee_id', 'total_price', 'temp_id'])
        details_output = self.details[['item_id', 'temp_id']]
        details_output.to_csv(self.details_path, index=False)
        output.to_csv(self.path, index=False)
        return output