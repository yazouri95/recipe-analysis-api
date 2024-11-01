# Food Recipes Analysis Application

## Overview

The **Food Recipes Analysis API** allows users to search recipes, retrieve detailed nutritional information, and calculate total
calorie counts with the ability to exclude specific ingredients. The API integrates with the Spoonacular API to retrieve
recipe and nutrition data and enables users to customize calorie calculations for dietary preferences or ingredient
restrictions.

### Prerequisites

- **Java 17**
- **Apache Maven (3.9.5+).**
- **Spoonacular API Key**: A valid API key from [Spoonacular](https://spoonacular.com/food-api) is required. Set this
  key in your application,yml.

## Steps to Run the Application

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yazouri95/recipe-analysis-api.git

2. Running the Application:
    ```
    $ mvn clean install
    $ mvn clean compile
    $ mvn spring-boot:run
    ```
   The app will start at http://localhost:8080

## API Endpoints

### 1. Search Recipes

- **Endpoint**: `GET /api/recipes/search`
- **Parameters**:
    - `query` (string): The search term, e.g., "pasta."
    - `pageNumber` (int): The page number (pagination).
    - `size` (int): The number of recipes per page.
- **Example Request**:
  ```http
  GET http://localhost:8080/api/recipes/search?query=pasta&pageNumber=0&size=10

### 2. Get Recipe Details

- **Endpoint**: `GET /api/recipes/{id}`
- **Parameters**:
    - `id` (long): The recipe ID
- **Example Request**:
  ```http
  GET http://localhost:8080/api/recipes/642583/info

### 3. Calculate Recipe Calories with Exclusions

- **Endpoint**: `GET /api/recipes/{id}/calories`
- **Parameters**:
    - `id` (long): The recipe ID
    - `excludedIngredientIds` (optional, comma-separated Ids): List of ingredients to exclude from calorie calculation.
- **Example Request**:
  ```http
  GET http://localhost:8080/api/recipes/642583/calories?excludedIngredientIds=99186,10120420