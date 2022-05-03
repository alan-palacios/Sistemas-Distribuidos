module.exports = {
  theme: {
    extend: {
      colors: {
        blue: '#77A2F5',
        blue_dark: '#5A8AE7',
        gray: '#7B7B7B',
        background: '#F5F5F5'
      },
      fontFamily:{
        sans: ['Montserrat', 'Helvetica', 'Arial', 'sans-serif']
      }
    },
  },
  plugins: [],
  content: [
    "./src/**/*.{js,jsx,ts,tsx,html}",
    "./public/index.html",
  ],
};
