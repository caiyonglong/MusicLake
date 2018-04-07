"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _index = _interopRequireDefault(require("./netease/index"));

var _index2 = _interopRequireDefault(require("./qq/index"));

var _index3 = _interopRequireDefault(require("./xiami/index"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

const api = {
  netease: _index.default,
  qq: _index2.default,
  xiami: _index3.default
};
const vendors = ['netease', 'qq', 'xiami'];
const app = {
  vendors: ['netease', 'qq', 'xiami'],
  paramsVerify: (vendor, id) => {
    // 参数校验
    if (!vendors.includes(vendor)) {
      return {
        status: false,
        msg: 'vendor错误'
      };
    }

    if (id.toString().trim().length < 1) {
      return {
        status: false,
        msg: 'id不能为空'
      };
    }
  },

  // 获取歌曲详情
  getSongDetail(vendor, id) {
    // 参数校验
    if (!this.vendors.includes(vendor)) {
      return {
        status: false,
        msg: 'vendor错误'
      };
    }

    if (id.toString().trim().length < 1) {
      return {
        status: false,
        msg: 'id不能为空'
      };
    }

    return api[vendor]['getSongDetail'](id);
  },

  // 搜索歌曲
  searchSong(keyword) {

      console.log("callByAndroid" +keyword)
    // 关键字不能为空
    if (!keyword || keyword.toString().trim().length < 1) {
      return {
        status: false,
        msg: '查询参数不能为空'
      };
    }

    return this.getData('searchSong', {
      keyword
    });
  },

  // 获取歌曲url
  getSongUrl(vendor, id) {
    // 参数校验
    if (!this.vendors.includes(vendor)) {
      return {
        status: false,
        msg: 'vendor错误'
      };
    }

    if (id.toString().trim().length < 1) {
      return {
        status: false,
        msg: 'id不能为空'
      };
    }

    return api[vendor]['getSongUrl'](id);
  },

  // 获取歌词
  getLyric(vendor, id) {
    // 参数校验
    if (!this.vendors.includes(vendor)) {
      return {
        status: false,
        msg: 'vendor错误'
      };
    }

    if (id.toString().trim().length < 1) {
      return {
        status: false,
        msg: 'id不能为空'
      };
    }

    return api[vendor]['getLyric'](id);
  },

  // 获取排行榜
  getTopList(id) {
    // id不能为空
    if (!id || id.toString().trim().length < 1) {
      return {
        status: false,
        msg: 'id不能为空'
      };
    }

    return _index.default.getTopList(id);
  },

  // 获取歌曲评论
  getComment(vendor, id, offset = 0, limit = 20) {
    this.paramsVerify(vendor, id);
    return api[vendor]['getComment'](id, offset, limit);
  },

  // 获取数据
  async getData(api, params) {
    let netease_rs = await _index.default[api](params);
    netease_rs = netease_rs.status ? netease_rs.data : [];
    let qq_rs = await _index2.default[api](params);
    qq_rs = qq_rs.status ? qq_rs.data : [];
    let xiami_rs = await _index3.default[api](params);
    xiami_rs = xiami_rs.status ? xiami_rs.data : [];
    return {
      status: true,
      data: {
        netease: netease_rs,
        qq: qq_rs,
        xiami: xiami_rs
      }
    };
  }

};
var _default = app;
exports.default = _default;