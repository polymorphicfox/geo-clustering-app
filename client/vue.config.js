module.exports = {
    publicPath: '/',

    devServer: {
        proxy: {
            '/public-api': {
                target: 'http://localhost:8081'
            }
        }
    },

};