import pandas as pd
import requests
import random
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import LabelEncoder
import joblib
import os

# -----------------------------
# 1. Load DummyJSON products
# -----------------------------
url = "https://dummyjson.com/products?limit=200"
data = requests.get(url).json()["products"]

df_api = pd.DataFrame(data)

df_api["base_price"] = df_api["price"]
df_api["demand_score"] = 0.6
df_api["rating"] = df_api["rating"]

# simulate market pricing
df_api["actual_price"] = df_api["price"] * (1 + df_api["rating"] / 10)

df_api = df_api[["category","brand","base_price","demand_score","rating","actual_price"]]


# -----------------------------
# 2. Add synthetic products
# (for manual admin products)
# -----------------------------
manual_categories = [
    "Electronics","Shoes","Food","Bottle","Mobile","Accessories"
]

manual_brands = [
    "Samsung","Apple","Puma","Nike","As-It-Is","MB","BOLDFit"
]

synthetic_data = []

for _ in range(500):

    category = random.choice(manual_categories)
    brand = random.choice(manual_brands)

    base_price = random.randint(100, 100000)
    demand_score = round(random.uniform(0.4,1.0),2)
    rating = round(random.uniform(3.5,5.0),1)

    actual_price = base_price * (1 + demand_score * 0.2) + rating * 80

    synthetic_data.append([
        category,brand,base_price,demand_score,rating,actual_price
    ])

df_manual = pd.DataFrame(
    synthetic_data,
    columns=[
        "category","brand","base_price",
        "demand_score","rating","actual_price"
    ]
)


# -----------------------------
# 3. Combine datasets
# -----------------------------
df = pd.concat([df_api, df_manual], ignore_index=True)


# -----------------------------
# 4. Encode categorical features
# -----------------------------
le_category = LabelEncoder()
le_brand = LabelEncoder()

df["category"] = le_category.fit_transform(df["category"].astype(str))
df["brand"] = le_brand.fit_transform(df["brand"].astype(str))


# -----------------------------
# 5. Train model
# -----------------------------
X = df[["category","brand","base_price","demand_score","rating"]]
y = df["actual_price"]

model = RandomForestRegressor(
    n_estimators=300,
    max_depth=10,
    random_state=42
)

model.fit(X,y)


# -----------------------------
# 6. Save model
# -----------------------------
os.makedirs("model", exist_ok=True)

joblib.dump(model,"model/price_model.pkl")
joblib.dump(le_category,"model/le_category.pkl")
joblib.dump(le_brand,"model/le_brand.pkl")

print("✅ Model trained with API + manual product data")