import http from "k6/http";
import { check } from "k6";

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080"; // Default to the server URL from the image

// Global variables to store the created restaurant and meal IDs
let restaurantId;
let mealId;
let reservationCode = "RES-INITIAL";


export const options = {
    stages: [
        // Ramp up to a moderate number of virtual users
        { duration: '10s', target: 30 },
        // Sustain the load for a longer period
        { duration: '30s', target: 30 },
        // Ramp down to zero
        { duration: '10s', target: 0 },
    ],
    thresholds: {
        'http_req_duration': ['p(95)<200'], // 95th percentile response time < 200ms
        'http_req_failed': ['rate<0.01'],    // Less than 1% of requests should fail
        'checks': ['rate>0.99'],           // 99% of checks should pass
    },
};

// Function to create a restaurant (will run once)
export function setup() {
  console.log("Setting up the test environment (creating restaurant and meal)...");

  // Create a restaurant
  const newRestaurantPayload = JSON.stringify({
      name: `Setup Restaurant ${Date.now()}`,
      location: "Setup Location",
      locationCode: 999,
      capacity: 1000000000,
      openingTime: "00:00",
      closingTime: "23:00",
  });
  const resRestaurantPost = http.post(`${BASE_URL}/restaurants`, newRestaurantPayload, {
      headers: { 'Content-Type': 'application/json' },
  });
  check(resRestaurantPost, {
      'Setup POST restaurant status is 201 or 200': (r) => r.status === 201 || r.status === 200,
  });
  restaurantId = resRestaurantPost.json().id;
  console.log(`Created restaurant with ID: ${restaurantId}`);

  // Create a meal
  const mealParams = {
      restaurantId: restaurantId,
      name: `Setup Meal ${Date.now()}`,
      price: 9.99,
  };
  const resMealPost = http.post(`${BASE_URL}/meals`, mealParams);
  check(resMealPost, {
      'Setup POST meal status is 200': (r) => r.status === 200,
  });
  mealId = resMealPost.json().id;
  console.log(`Created meal with ID: ${mealId}`);

  // Create an initial reservation
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  const dateTime = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  const reservationParams = {
      mealIds: [mealId],
      dateTime: dateTime,
      people: 1,
  };
  const resReservationPost = http.post(`${BASE_URL}/reservations`, reservationParams);
  check(resReservationPost, {
      'Setup POST reservation status is 200 or 201': (r) => r.status === 200 || r.status === 201,
  });
  if (resReservationPost.json() && resReservationPost.json().code) {
      reservationCode = resReservationPost.json().code;
      console.log(`Created initial reservation with code: ${reservationCode}`);
  }

  // Return the created IDs so they are available to the default function
  return { restaurantId: restaurantId, mealId: mealId, reservationCode: reservationCode };
}

export default function (data) {
  const currentRestaurantId = data.restaurantId;
  const currentReservationCode = data.reservationCode;

  // --- Restaurant Controller ---

  // GET /restaurants/{id}
  const resRestaurantGetOne = http.get(`${BASE_URL}/restaurants/${currentRestaurantId}`);
  check(resRestaurantGetOne, {
      'GET restaurant by ID status is 200': (r) => r.status === 200,
  });

  // GET /restaurants
  const resRestaurantGetAll = http.get(`${BASE_URL}/restaurants`);
  check(resRestaurantGetAll, {
      'GET all restaurants status is 200': (r) => r.status === 200,
      'GET all restaurants response is an array': (r) => Array.isArray(r.json()),
  });

  // --- Reservation Controller ---

  // GET /reservations/{code}
  const resReservationGetOne = http.get(`${BASE_URL}/reservations/${currentReservationCode}`);
  check(resReservationGetOne, {
      'GET reservation by code status is 200': (r) => r.status === 200,
  });

  // GET /reservations
  const resReservationGetAll = http.get(`${BASE_URL}/reservations`);
  check(resReservationGetAll, {
      'GET all reservations status is 200': (r) => r.status === 200,
      'GET all reservations response is an array': (r) => Array.isArray(r.json()),
  });

  // POST new reservation in each iteration
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  const dateTime = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  const people = 1;
  const reservationParams = {
      mealIds: [data.mealId], // Use the globally created meal ID
      dateTime: dateTime,
      people: people,
  };
  const resNewReservationPost = http.post(`${BASE_URL}/reservations`, reservationParams);
  check(resNewReservationPost, {
      'POST new reservation status is 200 or 201': (r) => r.status === 200 || r.status === 201,
  });
  console.log(`POST new reservation - Status: ${resNewReservationPost.status}, Body: ${resNewReservationPost.body}`);
}