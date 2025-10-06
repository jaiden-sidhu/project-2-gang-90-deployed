import pandas as pd
from sqlalchemy import create_engine, text
from pathlib import Path
import traceback

BASE_DIR = Path(__file__).resolve().parents[1]
DATA_DIR = BASE_DIR / "data"

TRANSACTIONS_FILE_PATH = DATA_DIR / "transactions.csv"
TRANSACTION_DETAILS_FILE_PATH = DATA_DIR / "transaction_details.csv"
TRANSACTIONS_EXPORT_PATH = DATA_DIR / "transactions_export.csv"
INGREDIENTS_FILE_PATH = DATA_DIR / "ingredients.csv"
INGREDIENTS_MAP_FILE_PATH = DATA_DIR / "ingredients_map.csv"
MENU_FILE_PATH = DATA_DIR / "menu.csv"
PERSONNEL_FILE_PATH = DATA_DIR / "personnel.csv"

TRANSACTIONS_TABLE_NAME = "transactions"
TRANSACTION_DETAILS_TABLE_NAME = "transaction_details"
INGREDIENTS_TABLE_NAME = "ingredients"
INGREDIENTS_MAP_TABLE_NAME = "ingredients_map"
MENU_TABLE_NAME = "menu"
PERSONNEL_TABLE_NAME = "personnel"

DB_USER = "gang_90"
DB_PASS = "gang_90"
DB_HOST = "csce-315-db.engr.tamu.edu"
DB_PORT = "5432"
DB_NAME = "gang_90_db"

def load_csv_to_postgres():
    try:
        if not TRANSACTIONS_FILE_PATH.exists():
            raise FileNotFoundError(f"Transactions file not found: {TRANSACTIONS_FILE_PATH}")
        if not TRANSACTION_DETAILS_FILE_PATH.exists():
            raise FileNotFoundError(f"Transaction details file not found: {TRANSACTION_DETAILS_FILE_PATH}")
        if not INGREDIENTS_FILE_PATH.exists():
            raise FileNotFoundError(f"Ingredients file not found: {INGREDIENTS_FILE_PATH}")
        if not INGREDIENTS_MAP_FILE_PATH.exists():
            raise FileNotFoundError(f"Ingredients map file not found: {INGREDIENTS_MAP_FILE_PATH}")
        if not MENU_FILE_PATH.exists():
            raise FileNotFoundError(f"Menu file not found: {MENU_FILE_PATH}")
        if not PERSONNEL_FILE_PATH.exists():
            raise FileNotFoundError(f"Personnel file not found: {PERSONNEL_FILE_PATH}")

        transactions_df = pd.read_csv(TRANSACTIONS_FILE_PATH)
        transaction_details_df = pd.read_csv(TRANSACTION_DETAILS_FILE_PATH)
        ingredients_df = pd.read_csv(INGREDIENTS_FILE_PATH)
        ingredients_map_df = pd.read_csv(INGREDIENTS_MAP_FILE_PATH)
        menu_df = pd.read_csv(MENU_FILE_PATH)
        personnel_df = pd.read_csv(PERSONNEL_FILE_PATH)

        engine = create_engine(
            f"postgresql+psycopg2://{DB_USER}:{DB_PASS}@{DB_HOST}:{DB_PORT}/{DB_NAME}",
            future=True
        )
        print("Connected to PostgreSQL.")

        with engine.begin() as conn:
            conn.execute(text(f"ALTER TABLE {TRANSACTIONS_TABLE_NAME} ADD COLUMN IF NOT EXISTS temp_id SERIAL;"))
            conn.execute(text(f"ALTER TABLE {TRANSACTION_DETAILS_TABLE_NAME} ADD COLUMN IF NOT EXISTS temp_id INT;"))

            conn.execute(text(f"ALTER SEQUENCE {TRANSACTIONS_TABLE_NAME}_transaction_id_seq RESTART WITH 1;"))
            conn.execute(text(f"ALTER SEQUENCE {TRANSACTION_DETAILS_TABLE_NAME}_detail_id_seq RESTART WITH 1;"))
            conn.execute(text(f"ALTER SEQUENCE {PERSONNEL_TABLE_NAME}_employee_id_seq RESTART WITH 1;"))
            conn.execute(text(f"ALTER SEQUENCE {INGREDIENTS_TABLE_NAME}_ingredient_id_seq RESTART WITH 1;"))
            # conn.execute(text(f"ALTER SEQUENCE {MENU_TABLE_NAME}_item_id_seq RESTART WITH 1;"))
            print("Added temp_id columns.")

            personnel_df.to_sql(
                PERSONNEL_TABLE_NAME,
                conn,
                if_exists="append",
                index=False,
                method="multi"
            )
            print(f"Loaded {len(personnel_df)} rows into '{PERSONNEL_TABLE_NAME}'.")

        with engine.begin() as conn:
            transactions_df.to_sql(
                TRANSACTIONS_TABLE_NAME,
                conn,
                if_exists="append",
                index=False,
                method="multi"
            )
            print(f"Loaded {len(transactions_df)} rows into '{TRANSACTIONS_TABLE_NAME}'.")

        with engine.begin() as conn:
            ingredients_df.to_sql(
                INGREDIENTS_TABLE_NAME,
                conn,
                if_exists="append",
                index=False,
                method="multi"
            )
            print(f"Loaded {len(ingredients_df)} rows into '{INGREDIENTS_TABLE_NAME}'.")

        with engine.begin() as conn:
            menu_df.to_sql(
                MENU_TABLE_NAME,
                conn,
                if_exists="append",
                index=False,
                method="multi"
            )
            print(f"Loaded {len(menu_df)} rows into '{MENU_TABLE_NAME}'.")

        with engine.begin() as conn:
            ingredients_map_df.to_sql(
                INGREDIENTS_MAP_TABLE_NAME,
                conn,
                if_exists="append",
                index=False,
                method="multi"
            )
            print(f"Loaded {len(ingredients_map_df)} rows into '{INGREDIENTS_MAP_TABLE_NAME}'.")

        with engine.connect() as conn:
            exported_transactions_df = pd.read_sql_table(TRANSACTIONS_TABLE_NAME, conn)
        exported_transactions_df.to_csv(TRANSACTIONS_EXPORT_PATH, index=False)
        print(f"Exported transactions table to '{TRANSACTIONS_EXPORT_PATH}'.")

        combined_df = pd.merge(
            transaction_details_df,
            exported_transactions_df,
            on="temp_id",
            how="left"
        )

        final_df = combined_df[["transaction_id", "item_id", "temp_id"]]

        with engine.begin() as conn:
            final_df.to_sql(
                TRANSACTION_DETAILS_TABLE_NAME,
                conn,
                if_exists="append",
                index=False,
                method="multi"
            )

            conn.execute(text(f"ALTER TABLE {TRANSACTIONS_TABLE_NAME} DROP COLUMN IF EXISTS temp_id;"))
            conn.execute(text(f"ALTER TABLE {TRANSACTION_DETAILS_TABLE_NAME} DROP COLUMN IF EXISTS temp_id;"))
            print("Removed temp_id columns.")

        print(f"Loaded {len(final_df)} rows into '{TRANSACTION_DETAILS_TABLE_NAME}'.")
        print("Loading complete.")

        return len(transactions_df), len(final_df)

    except Exception:
        print("Error:")
        traceback.print_exc()
        return None

if __name__ == "__main__":
    load_csv_to_postgres()
