# Central script to populate the database with initial data
import pandas as pd
from generate_transactions import TransactionGenerator


def main():
    # Generate transactions
    generator = TransactionGenerator(num_transactions=100, max_amount=500.0, path="scripts/data/transactions.csv", config_path = "scripts/config/transactions.yaml")
    df = generator.generateTransactions()
    print(f"Generated {len(df)} transactions and saved to data/transactions.csv")


if __name__ == "__main__":
    main()