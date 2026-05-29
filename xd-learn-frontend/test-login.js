// Test script to simulate login
console.log('Testing login flow...');

// Mock token data
const mockToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Iui2hee6p-euoeeQhuWRmCIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNzg0NzM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
const mockRefreshToken = 'mock-refresh-token';

// Test the auth functions
import { isLoggedIn, getCurrentUser, setToken, setRefreshToken, parseToken } from './src/utils/auth.js';

console.log('Initial isLoggedIn():', isLoggedIn());
console.log('Initial getCurrentUser():', getCurrentUser());

// Set tokens
setToken(mockToken);
setRefreshToken(mockRefreshToken);

console.log('After setting tokens:');
console.log('isLoggedIn():', isLoggedIn());
console.log('getCurrentUser():', getCurrentUser());

// Test parseToken function
console.log('parseToken(mockToken):', parseToken(mockToken));
