"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _axios = _interopRequireDefault(require("axios"));

var _instace = _interopRequireDefault(require("./instace"));

var _lyric_decode = _interopRequireDefault(require("../util/lyric_decode"));

require("isomorphic-fetch");

var _crypto = _interopRequireDefault(require("./crypto"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

const NEW_API_URL = 'http://acs.m.xiami.com/h5/';
var _default = {
  // 根据api获取虾米token
  async getXiamiToken(api) {
    try {
      const res = await fetch(`${NEW_API_URL}${api}/1.0/`);
      let token = Array.from(res.headers._headers['set-cookie']);
      token = token.map(i => i.split(';')[0].trim());
      const myToken = token[0].replace('_m_h5_tk=', '').split('_')[0];
      return {
        token,
        signedToken: myToken
      };
    } catch (e) {
      console.warn(e);
    }
  },

  async searchSong({
    keyword,
    limit = 30,
    offset = 0
  }) {
    const params = {
      v: '2.0',
      key: keyword,
      limit: limit,
      page: offset,
      r: 'search/songs',
      app_key: 1
    };

    try {
      let data = await _instace.default.post('/web?', params);
      return {
        status: true,
        data: {
          total: data.total,
          songs: data.songs.map(item => {
            return {
              album: {
                id: item.album_id,
                name: item.album_name,
                cover: item.album_logo.replace('http', 'https').replace('1.jpg', '2.jpg').replace('1.png', '4.png')
              },
              artists: [{
                id: item.artist_id,
                name: item.artist_name,
                avatar: item.artist_logo
              }],
              name: item.song_name,
              id: item.song_id,
              commentId: item.song_id,
              cp: !item.listen_file
            };
          })
        }
      };
    } catch (e) {
      return Promise.reject(e);
    }
  },

  async getSongDetail(id) {
    try {
      const api = 'mtop.alimusic.music.songservice.getsongs';
      const {
        token,
        signedToken
      } = await this.getXiamiToken(api);
      const appKey = 12574478;
      const queryStr = JSON.stringify({
        requestStr: JSON.stringify({
          header: {
            appId: 200,
            appVersion: 1000000,
            callId: new Date().getTime(),
            network: 1,
            platformId: 'mac',
            remoteIp: '192.168.1.101',
            resolution: '1178*778'
          },
          model: {
            songIds: [id]
          }
        })
      });
      const t = new Date().getTime();

      const sign = _crypto.default.MD5(`${signedToken}&${t}&${appKey}&${queryStr}`);

      const {
        data
      } = await (0, _axios.default)(`${NEW_API_URL}/${api}/1.0/`, {
        // resolveWithFullResponse: true,
        params: {
          appKey,
          // 会变化
          t,
          // 会变化
          sign,
          // 会变化
          api: 'mtop.alimusic.social.commentservice.getcommentlist',
          v: '1.0',
          type: 'originaljson',
          dataType: 'json',
          // 会变化
          data: queryStr
        },
        headers: {
          'host': 'acs.m.xiami.com',
          'content-type': 'application/x-www-form-urlencoded',
          'cookie': token.join(';') // 会变化

        }
      });

      if (data.ret[0].startsWith('SUCCESS')) {
        const info = data.data.data.songs[0];
        return {
          status: true,
          data: {
            album: {
              id: info.albumId,
              name: info.albumName,
              cover: info.albumLogo.replace('http', 'https').replace('1.jpg', '2.jpg').replace('1.png', '4.png')
            },
            artists: [{
              id: info.artistId,
              name: info.artistName,
              avatar: info.artistLogo
            }],
            name: info.songName,
            id: info.songId,
            commentId: info.songId,
            cp: !info.listenFiles.length
          }
        };
      } else {
        return {
          status: false,
          msg: data.ret[0].slice('::')[1],
          e: data
        };
      }
    } catch (e) {
      console.warn(e);
      return {
        status: false,
        msg: '请求失败',
        log: e
      };
    }
  },

  async getSongUrl(id) {
    try {
      let data = await _instace.default.get(`http://www.xiami.com/song/playlist/id/${id}/object_name/default/object_id/0/cat/json`);
      return {
        status: true,
        data: {
          url: this.parseLocation(data.trackList[0].location)
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

  async getLyric(id) {
    let lyric_url;

    try {
      let data = await _instace.default.get(`http://www.xiami.com/song/playlist/id/${id}/object_name/default/object_id/0/cat/json`);
      lyric_url = data.trackList[0].lyric_url;
    } catch (e) {
      return {
        status: true,
        data: [],
        log: e
      };
    }

    if (lyric_url) {
      try {
        let {
          data
        } = await (0, _axios.default)(lyric_url);
        return {
          status: true,
          data: (0, _lyric_decode.default)(data)
        };
      } catch (e) {
        return {
          status: true,
          data: [],
          log: e
        };
      }
    } else {
      return {
        status: true,
        data: [],
        log: '未获取到歌曲url'
      };
    }
  },

  async getComment(objectId, offset, pageSize) {
    try {
      const api = 'mtop.alimusic.social.commentservice.getcommentlist';
      const {
        token,
        signedToken
      } = await this.getXiamiToken(api);
      const appKey = 12574478;
      const queryStr = JSON.stringify({
        requestStr: JSON.stringify({
          header: {
            appId: 200,
            appVersion: 1000000,
            callId: new Date().getTime(),
            network: 1,
            platformId: 'mac',
            remoteIp: '192.168.1.101',
            resolution: '1178*778'
          },
          model: {
            objectId,
            // 会变化
            objectType: 'song',
            pagingVO: {
              page: offset + 1,
              pageSize
            }
          }
        })
      });
      const t = new Date().getTime();

      const sign = _crypto.default.MD5(`${signedToken}&${t}&${appKey}&${queryStr}`);

      const {
        data
      } = await (0, _axios.default)(`${NEW_API_URL}/${api}/1.0/`, {
        // resolveWithFullResponse: true,
        params: {
          appKey,
          // 会变化
          t,
          // 会变化
          sign,
          // 会变化
          api: 'mtop.alimusic.social.commentservice.getcommentlist',
          v: '1.0',
          type: 'originaljson',
          dataType: 'json',
          // 会变化
          data: queryStr
        },
        headers: {
          'host': 'acs.m.xiami.com',
          'content-type': 'application/x-www-form-urlencoded',
          'cookie': token.join(';') // 会变化

        }
      });

      if (data.ret[0].startsWith('SUCCESS')) {
        return {
          status: true,
          data: {
            hotComments: [],
            comments: data.data.data.commentVOList,
            total: data.data.data.pagingVO.count
          }
        };
      } else {
        return {
          status: false,
          msg: data.ret[0].slice('::')[1],
          e: data
        };
      }
    } catch (e) {
      console.warn(e);
      return {
        status: false,
        msg: '请求失败',
        log: e
      };
    }
  },

  parseLocation(location) {
    let head = parseInt(location.substr(0, 1));

    let _str = location.substr(1);

    let rows = head;
    let cols = parseInt(_str.length / rows) + 1;
    let output = '';
    let full_row;

    for (let i = 0; i < head; i++) {
      if ((_str.length - i) / head === parseInt(_str.length / head)) {
        full_row = i;
      }
    }

    for (let c = 0; c < cols; c++) {
      for (let r = 0; r < head; r++) {
        if (c === cols - 1 && r >= full_row) {
          continue;
        }

        let char;

        if (r < full_row) {
          char = _str[r * cols + c];
        } else {
          char = _str[cols * full_row + (r - full_row) * (cols - 1) + c];
        }

        output += char;
      }
    }

    return decodeURIComponent(output).replace(/\^/g, '0');
  }

};
exports.default = _default;