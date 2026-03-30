# 🛍️ ShopNest AI

An AI-powered full-stack eCommerce platform with **dynamic pricing** and **product recommendation system**, built using **Spring Boot, FastAPI (ML), Angular, and AWS/Render**.

---

## Live Link
# https://shop-nest-ai.vercel.app/

## 🚀 Features

### 🧠 AI-Powered Capabilities

* **Dynamic Price Prediction**

  * Uses Machine Learning (Random Forest) to predict optimal product prices
* **Smart Product Recommendations**

  * TF-IDF + Cosine Similarity based recommendation engine
* **Real-time ML API integration** via FastAPI

---

### 🛒 Core eCommerce Features

* Product listing with pagination
* Search & filtering
* Product details page
* Stock management
* Admin product creation
* Review system (optional/extendable)

---

### 🌐 Full Stack Architecture

* **Frontend:** Angular (deployed on Vercel)
* **Backend:** Spring Boot (deployed on Render / AWS)
* **ML Service:** FastAPI (deployed on Render)
* **Database:** PostgreSQL (Supabase)

---

## 🏗️ Architecture Overview

```
Angular (Frontend)
        ↓
Spring Boot (Backend API)
        ↓
FastAPI ML Service  →  ML Models (TF-IDF, RandomForest)
        ↓
PostgreSQL (Supabase)
```

---

## 🔗 Live APIs

* Backend API:
  https://shop-nest-ai-1.onrender.com

* ML Service API:
  https://shop-nest-ai.onrender.com

---

## ⚙️ Tech Stack

### Backend

* Java 21
* Spring Boot 4
* Spring Data JPA
* Hibernate
* REST APIs

### Frontend

* Angular
* RxJS
* Tailwind CSS

### AI / ML

* Python
* FastAPI
* Scikit-learn
* TF-IDF Vectorizer
* Cosine Similarity
* Random Forest Regressor

### Database

* PostgreSQL (Supabase)

### Deployment

* Render (Backend + ML)
* Vercel (Frontend)

---

## 🧠 Machine Learning Details

### 🔹 Price Prediction Model

* Algorithm: Random Forest Regressor
* Features:

  * Category
  * Brand
  * Base Price
  * Demand Score
  * Rating

---

### 🔹 Recommendation System

* Technique: TF-IDF + Cosine Similarity
* Input:

  * Product description / metadata
* Output:

  * Top 5 similar products

---

## 🔌 API Endpoints

### 🔹 ML Service

#### Price Prediction

```http
POST /predict-price
```

Request:

```json
{
  "category": "beauty",
  "brand": "maybelline",
  "base_price": 500,
  "demand_score": 0.7,
  "rating": 4.2
}
```

---

#### Recommendations (by description)

```http
POST /recommend
```

---

#### Recommendations (by product ID)

```http
GET /recommend/{product_id}
```

---

### 🔹 Backend API

#### Get Products

```http
GET /products
```

#### Get Product by ID

```http
GET /products/id/{id}
```

#### Get Recommendations

```http
GET /products/{id}/recommendations
```

---

## 🛠️ Environment Variables

### Backend (.env)

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres.<project-ref>
SPRING_DATASOURCE_PASSWORD=your_password

SPRING_JPA_HIBERNATE_DDL_AUTO=update
PORT=8080
```

---

### ML Service

```env
PORT=10000
```

---

## 📦 Installation (Local Setup)

### 1. Clone Repo

```bash
git clone https://github.com/your-username/shopnest-ai.git
cd shopnest-ai
```

---

### 2. Run ML Service

```bash
cd ml-service
pip install -r requirements.txt
python app.py
```

---

### 3. Run Backend

```bash
cd backend
./mvnw spring-boot:run
```

---

### 4. Run Frontend

```bash
cd frontend
npm install
ng serve
```

---

## ⚠️ Known Issues / Improvements

* Recommendation matching depends on product name similarity
* Can improve using:

  * Semantic embeddings (BERT)
  * Vector DB (Pinecone / FAISS)
* Review API needs validation improvements

---

## 🎯 Future Enhancements

* 🧠 Deep learning recommendations (BERT / embeddings)
* 🛒 Cart & payment integration
* 📊 Admin analytics dashboard
* 🔍 ElasticSearch for better search
* ☁️ Full AWS deployment (EC2 + RDS + S3)

---

## 👨‍💻 Author

**Milan Suryavanshi**

* AI / Data Science Intern
* Full Stack Developer

---

## ⭐ If you like this project

Give it a ⭐ on GitHub and feel free to contribute!
