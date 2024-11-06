/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
module.exports = {
  theme: {
    extend: {
      colors: {
        'blue': {
          500: '#0078d4', 
          600: '#106ebe',
          700: '#005a9e',
        }
      }
    }
  },
  
}