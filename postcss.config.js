import tailwindcss from './client/node_modules/tailwindcss/lib/index.js'
import autoprefixer from './client/node_modules/autoprefixer/lib/autoprefixer.js'

export default {
  plugins: [
    tailwindcss(),
    autoprefixer(),
  ],
}
