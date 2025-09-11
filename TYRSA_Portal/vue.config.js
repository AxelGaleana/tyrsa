module.exports = {
  transpileDependencies: ["vuetify"],
  publicPath: "/portal_tyrsa/",
  chainWebpack: config => {
    config.module
      .rule('vue')
      .test(/\.vue$/)
      .use('vue-loader')
      .loader('vue-loader')
      .end();
  }
};