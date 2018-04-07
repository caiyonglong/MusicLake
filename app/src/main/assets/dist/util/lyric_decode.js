const decode = function (str) {
  let list = str.split('\n');
  let lyric_arr = [];
  list.forEach(item => {
    const matchs = item.match(/((\[\d+:\d+\.\d+\])+)(.*)/);

    if (matchs && matchs[1]) {
      const t_array = matchs[1].match(/\[\d+:\d+\.\d+\]/g);
      t_array.forEach(item => {
        lyric_arr.push([item.substring(1, item.length - 1), matchs[3]]);
      });
    }
  });
  return lyric_arr.sort();
};

module.exports = decode;