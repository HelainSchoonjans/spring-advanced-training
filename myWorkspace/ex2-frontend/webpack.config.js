const path = require("path");
const webpack = require('webpack');
const { AureliaPlugin } = require("aurelia-webpack-plugin");
const HtmlWebpackPlugin = require('html-webpack-plugin');

const settings = {
    development: {
        apiEndpoint: "http://localhost:8080",
        oidc: {
            redirect_uri: "http://localhost:9000",
            scope: "openid email profile",
            client_id: "ex2-frontend",
            usenonce: true,
            response_mode: "fragment",
            endpoints: {
                login: "http://localhost:8282/auth/realms/my-realm/protocol/openid-connect/auth",
                logout: "http://localhost:8282/auth/realms/my-realm/protocol/openid-connect/logout",
                token: "http://localhost:8282/auth/realms/my-realm/protocol/openid-connect/token",
                userinfo: "http://localhost:8282/auth/realms/my-realm/protocol/openid-connect/userinfo"
            }
        }
    },
    production: {
        apiEndpoint: "...",
        oidc: {
	}
    }
}

module.exports = (env, options) => {

    const production = "production" === options.mode;

    return {
        devtool: production ? 'source-maps' : 'inline-source-map',
        entry: "aurelia-bootstrapper",

        output: {
            path: path.resolve(__dirname, "dist"),
            filename: "[name].[hash:4].js",
            chunkFilename: "[name].[hash:4].js"
        },
        resolve: {
            extensions: [".ts", ".js"],
            modules: ["src", "node_modules"].map(x => path.resolve(x)),
        },
        module: {
            rules: [
                // CSS required in templates cannot be extracted safely because Aurelia would try to require it again in runtime
                { test: /\.css$/i, use: 'style-loader', issuer: [{ not: [{ test: /\.html$/i }] }] },
                { test: /\.css$/i, use: 'css-loader', issuer: [{ test: /\.html$/i }] },
                { test: /\.ts$/i, use: "ts-loader" },
                { test: /\.html$/i, use: "html-loader" },
                { test: /\.(ttf|eot|woff|woff2)$/, use: "file-loader?name=fonts/[name].[ext]" },
                { test: /\.(jpe?g|png|gif|svg)$/i, use: "url-loader?name=images/[name].[hash:4].[ext]&limit=8000" }
            ]
        },
        plugins: [
            new webpack.DefinePlugin({
                __CONFIG__: JSON.stringify(settings[options.mode]),
                __PROD__: JSON.stringify(production),
            }),
            new AureliaPlugin(),
            new HtmlWebpackPlugin({ template: "./src/index.html", filename: "./index.html", inject: true })
        ],
        devServer: {
            historyApiFallback: true,
            hot: true,
            port: 9000
        }
    }
};
