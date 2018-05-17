"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _instace = _interopRequireDefault(require("./instace"));

var _lyric_decode = _interopRequireDefault(require("../util/lyric_decode"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

const top_list_all = {
  "0": ["云音乐新歌榜", "3779629"],
  "1": ["云音乐热歌榜", "3778678"],
  "2": ["网易原创歌曲榜", "2884035"],
  "3": ["云音乐飙升榜", "19723756"],
  "4": ["云音乐电音榜", "10520166"],
  "5": ["UK排行榜周榜", "180106"],
  "6": ["美国Billboard周榜", "60198"],
  "7": ["KTV嗨榜", "21845217"],
  "8": ["iTunes榜", "11641012"],
  "9": ["Hit FM Top榜", "120001"],
  "10": ["日本Oricon周榜", "60131"],
  "11": ["韩国Melon排行榜周榜", "3733003"],
  "12": ["韩国Mnet排行榜周榜", "60255"],
  "13": ["韩国Melon原声周榜", "46772709"],
  "14": ["中国TOP排行榜(港台榜)", "112504"],
  "15": ["中国TOP排行榜(内地榜)", "64016"],
  "16": ["香港电台中文歌曲龙虎榜", "10169002"],
  "17": ["华语金曲榜", "4395559"],
  "18": ["中国嘻哈榜", "1899724"],
  "19": ["法国 NRJ EuroHot 30周榜", "27135204"],
  "20": ["台湾Hito排行榜", "112463"],
  "21": ["Beatport全球电子舞曲榜", "3812895"]
};
var _default = {
  async searchSong({
    keyword,
    limit = 30,
    offset = 0,
    type = 1
  }) {
    // *(type)* 搜索单曲(1)，歌手(100)，专辑(10)，歌单(1000)，用户(1002)
    const params = {
      csrf_token: '',
      limit,
      type,
      s: keyword,
      offset
    };

    try {
      let {
        result
      } = await _instace.default.post('/weapi/cloudsearch/get/web', params);
      return {
        status: true,
        data: {
          total: result.songCount,
          songs: result.songs.map(item => {
            return {
              album: {
                id: item.al.id,
                name: item.al.name,
                cover: item.al.picUrl
              },
              artists: item.ar,
              name: item.name,
              id: item.id,
              commentId: item.id,
              cp: !item.privilege.cp
            };
          })
        }
      };
    } catch (e) {
      return {
        status: false,
        msg: '获取失败',
        log: e
      };
    }
  },

  async getSongDetail(id) {
    try {
      let data = await _instace.default.post('/weapi/v3/song/detail', {
        c: JSON.stringify([{
          id: id
        }]),
        ids: '[' + id + ']',
        csrf_token: ''
      });
      const info = data.songs[0];
      return {
        status: true,
        data: {
          album: {
            id: info.al.id,
            name: info.al.name,
            cover: info.al.picUrl
          },
          artists: info.ar,
          name: info.name,
          id: info.id,
          commentId: info.id,
          cp: !data.privileges[0].cp
        }
      };
    } catch (e) {
      return {
        status: false,
        msg: '请求失败',
        log: e
      };
    }
  },

  async getSongUrl(id) {
    return {
      status: true,
      data: {
        url: `http://music.163.com/song/media/outer/url?id=${id}.mp3`
      }
    };
    const params = {
      ids: [id],
      br: 999000,
      csrf_token: ''
    };

    try {
      let {
        data
      } = await _instace.default.post('/weapi/song/enhance/player/url', params);
    } catch (e) {
      return {
        status: false,
        msg: '获取失败',
        log: e
      };
    }
  },

  async getLyric(id) {
    try {
      let data = await _instace.default.post('/weapi/song/lyric?os=osx&id=' + id + '&lv=-1&kv=-1&tv=-1', {});

      if (data.lrc && data.lrc.lyric) {
        return {
          status: true,
          data: (0, _lyric_decode.default)(data.lrc.lyric)
        };
      } else {
        return {
          status: true,
          data: []
        };
      }
    } catch (e) {
      return {
        status: false,
        msg: '请求失败',
        log: e
      };
    }
  },

  async getTopList(id) {
    try {
      const {
        playlist,
        privileges
      } = await _instace.default.post('/weapi/v3/playlist/detail', {
        id: top_list_all[id][1],
        limit: 30,
        offset: 0,
        total: true,
        n: 1000,
        csrf_token: ""
      });
      return {
        status: true,
        data: {
          name: playlist.name,
          description: playlist.description,
          cover: playlist.coverImgUrl,
          playCount: playlist.playCount,
          list: playlist.tracks.map((item, i) => {
            return {
              album: {
                id: item.al.id,
                name: item.al.name,
                cover: item.al.picUrl
              },
              artists: item.ar,
              name: item.name,
              id: item.id,
              commentId: item.id,
              cp: !privileges[i].cp
            };
          })
        }
      };
    } catch (e) {
      return {
        status: false,
        msg: '获取失败',
        log: e
      };
    }
  },

  async getComment(rid, offset = 0, limit = 20) {
    try {
      let {
        hotComments,
        comments,
        total
      } = await _instace.default.post('/weapi/v1/resource/comments/R_SO_4_' + rid + '/?csrf_token=', {
        offset,
        rid,
        limit,
        csrf_token: ""
      });
      return {
        status: true,
        data: {
          hotComments,
          comments,
          total
        }
      };
    } catch (e) {
      return {
        status: false,
        msg: '请求失败',
        log: e
      };
    }
  }

};
exports.default = _default;