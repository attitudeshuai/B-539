/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            container: {
                center: true,
                padding: '2rem',
                screens: {
                    sm: '640px',
                    md: '768px',
                    lg: '1024px',
                    xl: '1280px',
                    '2xl': '1400px', // Increased max-width
                },
            },
            colors: {
                primary: '#409EFF',
                secondary: '#606266',
                success: '#67C23A',
                warning: '#E6A23C',
                danger: '#F56C6C',
                info: '#909399',
            }
        },
    },
    plugins: [],
}
