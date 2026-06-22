const fs = require('fs');
const path = require('path');

const mediaDir = "C:\\Users\\Admin\\.gemini\\antigravity\\brain\\4ab6e78f-db30-4d3a-9855-4c74f39dd704";

fs.readdirSync(mediaDir).forEach(file => {
    if (file.endsWith('.png')) {
        const filePath = path.join(mediaDir, file);
        const stats = fs.statSync(filePath);
        console.log(`${file}: ${stats.size} bytes`);
    }
});
