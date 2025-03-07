# 📂 FileManager - Fullstack med Spring Boot & Nginx

Detta är en **filhanteringsapplikation** byggd med **Spring Boot (backend)** och en **statisk frontend i Nginx**.

## 🚀 Funktioner
- 🗄️ **Spring Boot Backend** – Hanterar filuppladdningar & nedladdningar.
- 🎨 **Statisk Frontend i Nginx** – HTML, CSS & JS.
- 📦 **PostgreSQL i Docker** – Databas för att lagra metadata.
- 🔍 **Spring Boot Actuator** – Healthchecks och övervakning.
- 📊 **Docker Compose** – Automatiserad körning av alla tjänster.

---

## 🛠️ **Installation & Uppstart**
### **1️⃣ Klona projektet**
```bash
git clone https://github.com/ditt-repo/filemanager.git
cd filemanager
```

### **2️⃣ Sätt miljövariabler (rekommenderat)**
```bash
export DB_USER=postgres
export DB_PASSWORD=mysecretpassword
```
(Windows: använd `set DB_USER=postgres`)

### **3️⃣ Starta PostgreSQL, Backend & Frontend med Docker**
```bash
docker-compose up -d --build
```

### **4️⃣ Kontrollera att allt körs**
```bash
docker ps
```
Du borde se `frontend`, `backend` och `database` köras.

---

## 🔥 **Öppna applikationen**
| Tjänst | URL |
|--------|-----|
| **Frontend** | [http://localhost:3000](http://localhost:3000) |
| **Backend API** | [http://localhost:8080/api](http://localhost:8080/api) |
| **Actuator Health** | [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) |

---

## 🛑 **Stoppa och rensa containrar**
```bash
docker-compose down
```

Om du vill radera **ALL databasinformation**:
```bash
docker-compose down -v
```

---

## 🛠️ **Felsökning**
### 🔹 **Kolla backend-loggar**
```bash
docker logs -f backend
```
### 🔹 **Kolla frontend-loggar**
```bash
docker logs -f frontend
```
### 🔹 **Kolla databasstatus**
```bash
docker exec -it $(docker ps -qf "name=database") psql -U postgres -d filemanager
```
Kör:
```sql
\dt
```
för att se databastabeller.

---

## **🎯 Sammanfattning**
✅ **Backend i Spring Boot + PostgreSQL**  
✅ **Frontend i Nginx**  
✅ **Docker Compose för enkel körning**  
✅ **Actuator Healthchecks & Metrics**  
✅ **Dokumentation för installation & API**  

🔥 **Nu är ditt projekt redo att köras & testas!**

