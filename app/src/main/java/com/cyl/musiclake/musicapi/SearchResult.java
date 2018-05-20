package com.cyl.musiclake.musicapi;

import java.util.List;

/**
 * Created by master on 2018/5/15.
 */

public class SearchResult {

    /**
     * status : true
     * data : {"netease":{"total":600,"songs":[{"album":{"id":38534319,"name":"TEST","cover":"http://p1.music.126.net/R9Zx40i5oCv1xCnM2HBvDQ==/109951163275466550.jpg"},"artists":[{"id":13790052,"name":"AKA-趸趸","tns":[],"alias":[]},{"id":0,"name":"Junkky","tns":[],"alias":[]}],"name":"Test","id":556709500,"commentId":556709500,"cp":false},{"album":{"id":398399,"name":"Portal 2 (Songs to Test By) (Volume 3)","cover":"http://p1.music.126.net/cnDqZYGJwgaDq1TduTLZIQ==/1687750348643622.jpg"},"artists":[{"id":87366,"name":"Aperture Science Psychoacoustics Laboratory","tns":[],"alias":[]}],"name":"TEST","id":3939099,"commentId":3939099,"cp":false},{"album":{"id":38229323,"name":"Laboratory Vol.4","cover":"http://p1.music.126.net/vZrpIVEY1LsrQTy5s5Srcg==/109951163235027384.jpg"},"artists":[{"id":12070016,"name":"零","tns":[],"alias":[]}],"name":"TEST","id":550901763,"commentId":550901763,"cp":false},{"album":{"id":35947191,"name":"Bootleg","cover":"http://p1.music.126.net/T0d20qnOQsld5NYTQOZLGg==/18232101812337645.jpg"},"artists":[{"id":12603713,"name":"Quadrough","tns":[],"alias":[]}],"name":"Test","id":498367998,"commentId":498367998,"cp":false},{"album":{"id":2715661,"name":"Test","cover":"http://p1.music.126.net/-RvneWGV839m3jbKdJJr_A==/5832909185401982.jpg"},"artists":[{"id":792117,"name":"TheBlackParrot","tns":[],"alias":[]}],"name":"Test","id":28028418,"commentId":28028418,"cp":false},{"album":{"id":2591679,"name":"Test / 4ever","cover":"http://p1.music.126.net/uFmDQtzw3T-mAxOB0oQlXw==/5990139348542476.jpg"},"artists":[{"id":95465,"name":"Little Dragon","tns":[],"alias":[]}],"name":"Test","id":27225009,"commentId":27225009,"cp":false},{"album":{"id":38383187,"name":"Test","cover":"http://p1.music.126.net/ToCfzpaScTj4MgVfRyCdKA==/109951163252193194.jpg"},"artists":[{"id":13286754,"name":"杏坂","tns":[],"alias":[]}],"name":"Test","id":553660131,"commentId":553660131,"cp":false},{"album":{"id":36943039,"name":"test","cover":"http://p1.music.126.net/Gf5atu59mXUekutPAb2n5w==/109951163078985716.jpg"},"artists":[{"id":12090019,"name":"HM2","tns":[],"alias":[]}],"name":"test","id":523045073,"commentId":523045073,"cp":false},{"album":{"id":37587283,"name":"test","cover":"http://p1.music.126.net/J92RhdtS9s66djLW148U7g==/109951163141778760.jpg"},"artists":[{"id":0,"name":"Aspric","tns":[],"alias":[]}],"name":"test","id":537349922,"commentId":537349922,"cp":false},{"album":{"id":36123276,"name":"I Am the Future","cover":"http://p1.music.126.net/LqyXnBRP5ULS0tuEkLBc8A==/18300271533256014.jpg"},"artists":[{"id":12869740,"name":"Saimn-I","tns":[],"alias":[]}],"name":"Test...","id":503739369,"commentId":503739369,"cp":false},{"album":{"id":2273124,"name":"Changed","cover":"http://p1.music.126.net/42rAKqZOeoW8bI2guU8ICQ==/6639950720797163.jpg"},"artists":[{"id":185503,"name":"Mario & Vidis","tns":[],"alias":[]}],"name":"Test -  (Album Redo)","id":25677136,"commentId":25677136,"cp":false},{"album":{"id":37057676,"name":"Moz Test","cover":"http://p1.music.126.net/WszbE31zLUnzLNNMYcHjYw==/109951163092588764.jpg"},"artists":[{"id":12977888,"name":"Tiki Robots","tns":[],"alias":[]}],"name":"Test","id":526044080,"commentId":526044080,"cp":false},{"album":{"id":37060015,"name":"Wav Test (Mac)","cover":"http://p1.music.126.net/xkW-b04o4SHmHzoHV_YMqg==/109951163092908940.jpg"},"artists":[{"id":12977809,"name":"Betty Fnord","tns":[],"alias":[]}],"name":"Test","id":526139184,"commentId":526139184,"cp":false},{"album":{"id":38006452,"name":"Una Minúscula Parte De Mi Existencia","cover":"http://p1.music.126.net/I4-WmO0MoQwcVkPTkjgtHw==/109951163202514092.jpg"},"artists":[{"id":13661117,"name":"Pekado","tns":[],"alias":[]}],"name":"Test","id":547168459,"commentId":547168459,"cp":false},{"album":{"id":38279375,"name":"Underground Indie Hip Hop, Vol. 1","cover":"http://p1.music.126.net/ZOTFMb63DwXeg0k526ShJg==/109951163238441259.jpg"},"artists":[{"id":13117638,"name":"Ricky Jones Band","tns":[],"alias":[]}],"name":"Test","id":551469227,"commentId":551469227,"cp":false},{"album":{"id":38295700,"name":"Animal Party Music, Vol. 2","cover":"http://p1.music.126.net/fQIM-8kjblucv-TQp6e0UQ==/109951163241353385.jpg"},"artists":[{"id":13062756,"name":"Kids Party Kings","tns":[],"alias":[]}],"name":"Test","id":551892209,"commentId":551892209,"cp":false},{"album":{"id":38527113,"name":"Pop Dance Drive , Vol. 10 (Instrumental)","cover":"http://p1.music.126.net/KFenoaJ26DwxlplNtJ8qjg==/109951163271557770.jpg"},"artists":[{"id":13106899,"name":"San Jackson Band","tns":[],"alias":[]}],"name":"Test","id":556176434,"commentId":556176434,"cp":false},{"album":{"id":48461,"name":"海猿~オリジナル・サウンドトラック","cover":"http://p1.music.126.net/26K5fAfYdrE76OcUdJc-Ug==/815837627809891.jpg"},"artists":[{"id":15325,"name":"佐藤直紀","tns":[],"alias":[]}],"name":"Test","id":510519,"commentId":510519,"cp":false},{"album":{"id":3175054,"name":"lIFE","cover":"http://p1.music.126.net/6hcCqtv5TtwjqRgW0Tbh2g==/7962663209241071.jpg"},"artists":[{"id":1087660,"name":"ANK","tns":[],"alias":[]}],"name":"Test","id":32957311,"commentId":32957311,"cp":false},{"album":{"id":37486177,"name":"Euro Dance Compilation, Vol. 1 (Special Edition)","cover":"http://p1.music.126.net/kqKGSxXwZ1sTBlLn5gpaLQ==/109951163128829565.jpg"},"artists":[{"id":13086340,"name":"Workout Jam Company","tns":[],"alias":[]}],"name":"Test","id":535005711,"commentId":535005711,"cp":false},{"album":{"id":37526534,"name":"Electro Gurus: Mixologies, Vol. 15","cover":"http://p1.music.126.net/ufu3StIjgoG01YabSTEC3Q==/109951163133140640.jpg"},"artists":[{"id":389940,"name":"Tunefunk","tns":[],"alias":[]},{"id":13077437,"name":"Deep House","tns":[],"alias":[]}],"name":"Test","id":535773517,"commentId":535773517,"cp":false},{"album":{"id":115217,"name":"Underwear","cover":"http://p1.music.126.net/FcC8JIyU1C9ydc6AcHDWUQ==/889504906914891.jpg"},"artists":[{"id":29532,"name":"Bobo Stenson","tns":[],"alias":[]}],"name":"Test","id":1111982,"commentId":1111982,"cp":false},{"album":{"id":37885169,"name":"Pressure Placement","cover":"http://p1.music.126.net/Eq_uF108M8-XfaWlG3Ft-A==/109951163177885932.jpg"},"artists":[{"id":13496416,"name":"Slaughterhouse Revolt","tns":[],"alias":[]}],"name":"Test","id":543998012,"commentId":543998012,"cp":false},{"album":{"id":137837,"name":"Devotion, Discipline, And Denial","cover":"http://p1.music.126.net/pLIHoGDK7Jq2yxNeSDuzog==/6652045348253011.jpg"},"artists":[{"id":33106,"name":"ESA","tns":[],"alias":[]}],"name":"Test","id":1339605,"commentId":1339605,"cp":false},{"album":{"id":2588386,"name":"Original Album Series","cover":"http://p1.music.126.net/-URpzNkLlwaNlTkPjJyTqg==/1253443255705942.jpg"},"artists":[{"id":66394,"name":"Ministry","tns":[],"alias":[]}],"name":"Test","id":27123965,"commentId":27123965,"cp":false},{"album":{"id":196787,"name":"Silver Sun","cover":"http://p1.music.126.net/iEPCvz1ORichsfUgA_eApQ==/919191720872382.jpg"},"artists":[{"id":43069,"name":"Silver Sun","tns":[],"alias":[]}],"name":"Test","id":1954419,"commentId":1954419,"cp":false},{"album":{"id":1947387,"name":"Little Dragon","cover":"http://p1.music.126.net/GqQvCeCntVK_CAtE4Zbv9g==/614626999937545.jpg"},"artists":[{"id":95465,"name":"Little Dragon","tns":[],"alias":[]}],"name":"Test","id":21041515,"commentId":21041515,"cp":false},{"album":{"id":36433656,"name":"Temblor","cover":"http://p1.music.126.net/c6zTuCyxyIjkl9Q5pJyLWw==/18852226369881060.jpg"},"artists":[{"id":344542,"name":"Tiko Taco","tns":[],"alias":[]}],"name":"Test","id":509377435,"commentId":509377435,"cp":false},{"album":{"id":36560599,"name":"Interview","cover":"http://p1.music.126.net/9C8kjGg1tTpQTsIFroAI2A==/18263987649554704.jpg"},"artists":[{"id":743363,"name":"Dmitry Ashin","tns":[],"alias":[]}],"name":"Test","id":513064259,"commentId":513064259,"cp":false},{"album":{"id":2711112,"name":"Body Language 13 Mixed by Azari & III","cover":"http://p1.music.126.net/f5lombJ6aZT-E-5HMJxcyg==/5775734580743512.jpg"},"artists":[{"id":83394,"name":"Unknown","tns":[],"alias":[]}],"name":"Test","id":27998356,"commentId":27998356,"cp":false}]},"qq":{"keyword":"test","total":393,"songs":[{"album":{"id":1290825,"name":"Smooth","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003jp3h81eT9Qn.jpg"},"artists":[{"id":1468082,"mid":"0036un4y2Vgsgq","name":"梁晓雪Kulu","title":"梁晓雪Kulu","title_hilight":"梁晓雪Kulu","type":0,"uin":0}],"name":"Test","id":"004BNnZO3rIZaT","commentId":105642629,"cp":false},{"album":{"id":1458856,"name":"Amour","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M0000045hqfV45jWbU.jpg"},"artists":[{"id":1165317,"mid":"000Oogsa22W0f7","name":"小瞳","title":"小瞳","title_hilight":"小瞳","type":0,"uin":0}],"name":"TEST","id":"004fGk2i4dD9ce","commentId":107192286,"cp":false},{"album":{"id":4931,"name":"The Mind Is A Terrible Thing To Taste","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004fA5bk1eFUMS.jpg"},"artists":[{"id":1658,"mid":"000HYOzT3e0UAH","name":"Ministry","title":"Ministry","title_hilight":"Ministry","type":2,"uin":0}],"name":"Test","id":"0017S7I23IAGCc","commentId":70311,"cp":false},{"album":{"id":283081,"name":"My Sound Is Technotronika Vol. 1","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001NrKGH0L5hEU.jpg"},"artists":[{"id":6658,"mid":"002BUZcl2OSALk","name":"Various Artists","title":"Various Artists","title_hilight":"Various Artists","type":3,"uin":0}],"name":"Test","id":"0023REhr0t5HX6","commentId":3431223,"cp":false},{"album":{"id":281707,"name":"Trance Trip","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003hh5XL4B7kFE.jpg"},"artists":[{"id":6658,"mid":"002BUZcl2OSALk","name":"Various Artists","title":"Various Artists","title_hilight":"Various Artists","type":3,"uin":0}],"name":"Test (Sol 7 Rmx)","id":"003So4r53fbM7R","commentId":3410243,"cp":false},{"album":{"id":130584,"name":"Cleansing","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003ieVow4N2hQX.jpg"},"artists":[{"id":52150,"mid":"000ddjO84DMqrM","name":"Prong","title":"Prong","title_hilight":"Prong","type":2,"uin":0}],"name":"Test","id":"001zWuLw2Sz7j4","commentId":1663915,"cp":false},{"album":{"id":650121,"name":"Original Album Classics","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003SyfWb0xZYu7.jpg"},"artists":[{"id":52150,"mid":"000ddjO84DMqrM","name":"Prong","title":"Prong","title_hilight":"Prong","type":2,"uin":0}],"name":"Test","id":"004ZzO340EWteI","commentId":7132662,"cp":false},{"album":{"id":138354,"name":"A Foolish Seperation","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001lmOOk2TsINE.jpg"},"artists":[{"id":7343,"mid":"002G6BRs2nHYpz","name":"정재욱","title":"정재욱 (郑在旭)","title_hilight":"정재욱 (郑在旭)","type":0,"uin":0}],"name":"Test","id":"002naaMO0ep7At","commentId":102818009,"cp":false},{"album":{"id":1635591,"name":"THE TIME MACHINE EP.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004cq1ag45pTrm.jpg"},"artists":[{"id":1248593,"mid":"000Xk2Gb25auxv","name":"Controller","title":"Controller","title_hilight":"Controller","type":0,"uin":0}],"name":"TEST","id":"000doGLv0JjWeh","commentId":108793304,"cp":false},{"album":{"id":1635591,"name":"THE TIME MACHINE EP.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004cq1ag45pTrm.jpg"},"artists":[{"id":1248593,"mid":"000Xk2Gb25auxv","name":"Controller","title":"Controller","title_hilight":"Controller","type":0,"uin":0}],"name":"TEST","id":"003vR4KZ1IfDTt","commentId":108793305,"cp":false},{"album":{"id":1787967,"name":"In Loving Memory","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003fEWt31xmzsk.jpg"},"artists":[{"id":225565,"mid":"004aJO2w3CNL5P","name":"Saro","title":"Saro","title_hilight":"Saro","type":0,"uin":0}],"name":"Test","id":"001oJFyB23tosE","commentId":200185612,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"Some","id":"002Sfk6p0d5pfu","commentId":3757851,"cp":false},{"album":{"id":1550416,"name":"Test","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000VBNV20xqiR5.jpg"},"artists":[{"id":1103039,"mid":"002jCDoK21WmGh","name":"HOPE-T","title":"HOPE-T","title_hilight":"HOPE-T","type":0,"uin":0}],"name":"Casual","id":"003rQqn14dqhox","commentId":107958927,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"西吧，阿西吧","id":"0022H8B30WLAf6","commentId":106867368,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"Little Thing","id":"003NS9jJ2QUV0r","commentId":3757852,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"朋友，再见","id":"0029z9tS0gHs7n","commentId":106867376,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"找见龙王，总之很难","id":"000lov4m0VQ4JQ","commentId":106867375,"cp":false},{"album":{"id":1476002,"name":"F.B.G.: The Movie (Gangsta Grillz)","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M0000010kXNu0fyUMC.jpg"},"artists":[{"id":35018,"mid":"003rLetL0AcGNH","name":"Future","title":"Future","title_hilight":"Future"},{"id":767527,"mid":"003UT7kY2d00fa","name":"Test","title":"Test","title_hilight":"<em>Test<\/em>"},{"id":18949,"mid":"000aGiRX2GUdef","name":"Sisqo","title":"Sisqo","title_hilight":"Sisqo"}],"name":"See It To Believe It","id":"002Lb7a9382q0X","commentId":107321624,"cp":false},{"album":{"id":1766705,"name":"Avenir en suspens","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001RrRRk0IYZu6.jpg"},"artists":[{"id":1311315,"mid":"001sF5rv0LKSPT","name":"Zakariens","title":"Zakariens","title_hilight":"Zakariens"},{"id":1311394,"mid":"000Yryu727d0NZ","name":"Test","title":"Test","title_hilight":"<em>Test<\/em>"},{"id":976026,"mid":"003zLYMX16cRih","name":"Reeno","title":"Reeno","title_hilight":"Reeno"},{"id":1084791,"mid":"002j5f0t48ient","name":"Aickone","title":"Aickone","title_hilight":"Aickone"}],"name":"Avec ce qu'on a","id":"002G0jEw3z8dU1","commentId":109931311,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"More Then Friends","id":"001MEpPL4Wsv5Z","commentId":210492299,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"He Touched Me","id":"002d8Smv2I5jXB","commentId":210492303,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"Show The World","id":"002Fxm0B2f38rq","commentId":210492297,"cp":false},{"album":{"id":2697779,"name":"3 Cups","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001V2Qfm3gfu76.jpg"},"artists":[{"id":1536089,"mid":"002DUFmA3T1Vrq","name":"test","title":"test","title_hilight":"<em>test<\/em>","type":0,"uin":0}],"name":"3 Cups","id":"002p3yaT3R79IB","commentId":207594839,"cp":false},{"album":{"id":87377,"name":"TEST DRIVE featuring JASON DERULO","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004PHLzA3AzERv.jpg"},"artists":[{"id":13937,"mid":"0044WixB1X6VMh","name":"赤西仁","title":"赤西仁 (Jin Akanishi)","title_hilight":"赤西仁 (Jin Akanishi)","type":0,"uin":0}],"name":"TEST DRIVE","id":"002cDlol0QtQhy","commentId":1026486,"cp":false},{"album":{"id":87377,"name":"TEST DRIVE featuring JASON DERULO","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004PHLzA3AzERv.jpg"},"artists":[{"id":13937,"mid":"0044WixB1X6VMh","name":"赤西仁","title":"赤西仁 (Jin Akanishi)","title_hilight":"赤西仁 (Jin Akanishi)","type":0,"uin":0}],"name":"TEST DRIVE","id":"000XxtwE38gnYe","commentId":1026489,"cp":false},{"album":{"id":2251222,"name":"This Is A Test (Remixes)","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000002KfOSo3m2uhu.jpg"},"artists":[{"id":9371,"mid":"0029cGzw2tZTrQ","name":"Armin Van Buuren","title":"Armin Van Buuren","title_hilight":"Armin Van Buuren","type":0,"uin":0}],"name":"This Is A Test","id":"0030Yzd821kC1Z","commentId":203939619,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"绿色大裤衩","id":"001wxR5n4DoPwH","commentId":106867367,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"朋友，你好","id":"0046aLqv2bBHbm","commentId":106867365,"cp":false},{"album":{"id":1225474,"name":"Test","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000002bzUqO3TwvQa.jpg"},"artists":[{"id":1077138,"mid":"000XT1281d9SoZ","name":"Rosper","title":"Rosper","title_hilight":"Rosper","type":0,"uin":0}],"name":"Synthetic","id":"003k2ibZ099cSS","commentId":105128588,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"At A Loss","id":"000Kpxov2Vud32","commentId":3757842,"cp":false}]},"xiami":{"total":5261,"songs":[{"album":{"id":497593,"name":"海猿～オリジナル・サウンドトラック","cover":"https://pic.xiami.net/images/album/img19/32319/4975931330507218_2.jpg"},"artists":[{"id":32319,"name":"佐藤直紀","avatar":"http://pic.xiami.net/images/artist/12320839104109_1.jpg"}],"name":"Test","id":1770842424,"commentId":1770842424,"cp":false},{"album":{"id":2102710027,"name":"Mike Gao - Shifty ft. Anderson Paak and Doja Cat","cover":"https://pic.xiami.net/images/album/img48/125148/2818601489281860_2.jpg"},"artists":[{"id":125148,"name":"Mike Gao","avatar":"http://pic.xiami.net/images/artistlogo/43/14892798745043_1.jpg"}],"name":"Mike Gao - Shifty ft. Anderson Paak & Doja Cat","id":1795630336,"commentId":1795630336,"cp":false},{"album":{"id":2102660539,"name":"Da Rhyizm","cover":"https://pic.xiami.net/images/album/img22/131/5847ac282dfef_6569022_1481092136_2.jpg"},"artists":[{"id":120840,"name":"Da-little","avatar":"http://pic.xiami.net/images/artistlogo/51/13518357891951_1.jpg"}],"name":"glow feat. [TEST]","id":1795318198,"commentId":1795318198,"cp":false},{"album":{"id":180757,"name":"Fast Times at Barrington High","cover":"https://pic.xiami.net/images/album/img9/26709/180757_2.jpg"},"artists":[{"id":26709,"name":"The Academy Is...","avatar":"http://pic.xiami.net/images/artist/12196331048090_1.jpg"}],"name":"Test ","id":3031975,"commentId":3031975,"cp":false},{"album":{"id":8746,"name":"Jade-1","cover":"https://pic.xiami.net/images/album/img69/1569/87461350287062_2.jpg"},"artists":[{"id":1569,"name":"关心妍","avatar":"http://pic.xiami.net/images/artistlogo/73/14359202865773_1.jpg"}],"name":"考验","id":108720,"commentId":108720,"cp":false},{"album":{"id":2102726006,"name":"WWE: The Music, Vol. 4","cover":"https://pic.xiami.net/images/album/img47/535947/15060931490535947_2.jpg"},"artists":[{"id":111645,"name":"Jim Johnston","avatar":"http://pic.xiami.net/images/artistlogo/3/13395894167103_1.png"}],"name":"This Is a Test","id":1795736633,"commentId":1795736633,"cp":false},{"album":{"id":2102669778,"name":"Test","cover":"https://pic.xiami.net/images/album/img72/105/5858b952c0d42_5290672_1482209618_2.jpg"},"artists":[{"id":2110190497,"name":"Saro","avatar":"http://pic.xiami.net/images/artistlogo/38/14822092714038_1.jpg"}],"name":"Test","id":1795405468,"commentId":1795405468,"cp":false},{"album":{"id":1928386911,"name":"Dfsda","cover":"https://pic.xiami.net/images/album/img10/878106010/19283869111428386911_2.jpg"},"artists":[{"id":878106010,"name":"我是红茶至尊官方账号","avatar":"http://pic.xiami.net/images/artistlogo/31/15258315582531_1.jpg"}],"name":"test","id":1774144132,"commentId":1774144132,"cp":false},{"album":{"id":2102669774,"name":"In Loving Memory","cover":"https://pic.xiami.net/images/album/img40/2/5858b85fe57d9_105840_1482209375_2.jpg"},"artists":[{"id":2110190497,"name":"Saro","avatar":"http://pic.xiami.net/images/artistlogo/38/14822092714038_1.jpg"}],"name":"Test","id":1795405442,"commentId":1795405442,"cp":false},{"album":{"id":469689,"name":"Portal 2 (Songs to Test By) (Volume 3)","cover":"https://pic.xiami.net/images/album/img71/98071/4696891318359739_2.jpg"},"artists":[{"id":98071,"name":"Aperture Science Psychoacoustics Laboratory","avatar":"http://pic.xiami.net/images/artistlogo/93/13182725835693_1.jpg"}],"name":"TEST","id":1770532276,"commentId":1770532276,"cp":false},{"album":{"id":1500354893,"name":"Best Of","cover":"https://pic.xiami.net/images/album/img92/354892/3548921400354892_2.jpg"},"artists":[{"id":43565,"name":"Little Dragon","avatar":"http://pic.xiami.net/images/artist/79/12601802321679_1.jpg"}],"name":"Test","id":1773196224,"commentId":1773196224,"cp":false},{"album":{"id":253590,"name":"Little Dragon","cover":"https://pic.xiami.net/images/album/img90/253590/j06237k1ybz_2.jpg"},"artists":[{"id":43565,"name":"Little Dragon","avatar":"http://pic.xiami.net/images/artist/79/12601802321679_1.jpg"}],"name":"Test","id":2904033,"commentId":2904033,"cp":false},{"album":{"id":2102760301,"name":"Test","cover":"https://pic.xiami.net/images/album/img30/297030/1928361496297030_2.jpg"},"artists":[{"id":1630974860,"name":"Gelvetta","avatar":"http://pic.xiami.net/images/artistlogo/48/14309748606548_1.jpg"}],"name":"Test","id":1795948776,"commentId":1795948776,"cp":false},{"album":{"id":126055014,"name":"Test (Original Motion Picture Soundtrack)","cover":"https://pic.xiami.net/images/album/img14/626055014/1260550141426055014_2.jpg"},"artists":[{"id":626055014,"name":"Ceiri Torjussen","avatar":"http://pic.xiami.net/images/artistlogo/36/14260550141236_1.jpg"}],"name":"Xxx Dance","id":1774055962,"commentId":1774055962,"cp":false},{"album":{"id":2100331602,"name":"测试-TEST","cover":"https://pic.xiami.net/images/album/img88/2100022988/21003316021462647732_2.jpg"},"artists":[{"id":2100022988,"name":"Yippee","avatar":"http://pic.xiami.net/images/artistlogo/47/14626997427747_1.jpg"}],"name":"朋友，再见(Bye,see u)","id":1776043572,"commentId":1776043572,"cp":false},{"album":{"id":566351813,"name":"泡泡糖test","cover":"https://pic.xiami.net/images/album/img16/166351116/5663518131366351822_2.jpg"},"artists":[{"id":166351116,"name":"大大泡泡糖","avatar":"http://pic.xiami.net/images/artistlogo/65/13663526734065_1.jpg"}],"name":"01 - Girl's Carnival (Live)","id":1771824356,"commentId":1771824356,"cp":false},{"album":{"id":400153,"name":"Devotion, Discipline, And Denial","cover":"https://pic.xiami.net/images/album/img13/72513/4001531283410212_2.jpg"},"artists":[{"id":72513,"name":"ESA","avatar":"http://pic.xiami.net/images/artist/56/12694887073356_1.jpg"}],"name":"Test","id":1769744462,"commentId":1769744462,"cp":false},{"album":{"id":430231,"name":"The House","cover":"https://pic.xiami.net/images/album/img85/87185/4302311299819472_2.jpg"},"artists":[{"id":87185,"name":"Romantic Couch","avatar":"http://pic.xiami.net/images/artistlogo/73/13014734264173_1.jpg"}],"name":"Test (Makes No Sense)","id":1770084411,"commentId":1770084411,"cp":false},{"album":{"id":278675,"name":"Underwear","cover":"https://pic.xiami.net/images/album/img7/49307/2786751237780423_2.jpg"},"artists":[{"id":49307,"name":"Bobo Stenson","avatar":"http://pic.xiami.net/images/artist/12270632508521_1.jpg"}],"name":"Test","id":3132914,"commentId":3132914,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484888,"commentId":1770484888,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484889,"commentId":1770484889,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484890,"commentId":1770484890,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484891,"commentId":1770484891,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484892,"commentId":1770484892,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484894,"commentId":1770484894,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484895,"commentId":1770484895,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484897,"commentId":1770484897,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484898,"commentId":1770484898,"cp":false},{"album":{"id":468881,"name":"Stuck In This Ocean","cover":"https://pic.xiami.net/images/album/img9/97609/4688811317286130_2.jpg"},"artists":[{"id":97609,"name":"Airship","avatar":"http://pic.xiami.net/images/artistlogo/31/13172753748831_1.jpg"}],"name":"Test","id":1770522595,"commentId":1770522595,"cp":false},{"album":{"id":1275767700,"name":"Dino Valente","cover":"https://pic.xiami.net/images/album/img72/2075755272/12757677001375767700_2.jpg"},"artists":[{"id":2075755272,"name":"Dino Valente","avatar":"http://pic.xiami.net/images/artistlogo/38/13757552726038_1.png"}],"name":"Test","id":1772076510,"commentId":1772076510,"cp":false}]}}
     */

    private boolean status;
    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * netease : {"total":600,"songs":[{"album":{"id":38534319,"name":"TEST","cover":"http://p1.music.126.net/R9Zx40i5oCv1xCnM2HBvDQ==/109951163275466550.jpg"},"artists":[{"id":13790052,"name":"AKA-趸趸","tns":[],"alias":[]},{"id":0,"name":"Junkky","tns":[],"alias":[]}],"name":"Test","id":556709500,"commentId":556709500,"cp":false},{"album":{"id":398399,"name":"Portal 2 (Songs to Test By) (Volume 3)","cover":"http://p1.music.126.net/cnDqZYGJwgaDq1TduTLZIQ==/1687750348643622.jpg"},"artists":[{"id":87366,"name":"Aperture Science Psychoacoustics Laboratory","tns":[],"alias":[]}],"name":"TEST","id":3939099,"commentId":3939099,"cp":false},{"album":{"id":38229323,"name":"Laboratory Vol.4","cover":"http://p1.music.126.net/vZrpIVEY1LsrQTy5s5Srcg==/109951163235027384.jpg"},"artists":[{"id":12070016,"name":"零","tns":[],"alias":[]}],"name":"TEST","id":550901763,"commentId":550901763,"cp":false},{"album":{"id":35947191,"name":"Bootleg","cover":"http://p1.music.126.net/T0d20qnOQsld5NYTQOZLGg==/18232101812337645.jpg"},"artists":[{"id":12603713,"name":"Quadrough","tns":[],"alias":[]}],"name":"Test","id":498367998,"commentId":498367998,"cp":false},{"album":{"id":2715661,"name":"Test","cover":"http://p1.music.126.net/-RvneWGV839m3jbKdJJr_A==/5832909185401982.jpg"},"artists":[{"id":792117,"name":"TheBlackParrot","tns":[],"alias":[]}],"name":"Test","id":28028418,"commentId":28028418,"cp":false},{"album":{"id":2591679,"name":"Test / 4ever","cover":"http://p1.music.126.net/uFmDQtzw3T-mAxOB0oQlXw==/5990139348542476.jpg"},"artists":[{"id":95465,"name":"Little Dragon","tns":[],"alias":[]}],"name":"Test","id":27225009,"commentId":27225009,"cp":false},{"album":{"id":38383187,"name":"Test","cover":"http://p1.music.126.net/ToCfzpaScTj4MgVfRyCdKA==/109951163252193194.jpg"},"artists":[{"id":13286754,"name":"杏坂","tns":[],"alias":[]}],"name":"Test","id":553660131,"commentId":553660131,"cp":false},{"album":{"id":36943039,"name":"test","cover":"http://p1.music.126.net/Gf5atu59mXUekutPAb2n5w==/109951163078985716.jpg"},"artists":[{"id":12090019,"name":"HM2","tns":[],"alias":[]}],"name":"test","id":523045073,"commentId":523045073,"cp":false},{"album":{"id":37587283,"name":"test","cover":"http://p1.music.126.net/J92RhdtS9s66djLW148U7g==/109951163141778760.jpg"},"artists":[{"id":0,"name":"Aspric","tns":[],"alias":[]}],"name":"test","id":537349922,"commentId":537349922,"cp":false},{"album":{"id":36123276,"name":"I Am the Future","cover":"http://p1.music.126.net/LqyXnBRP5ULS0tuEkLBc8A==/18300271533256014.jpg"},"artists":[{"id":12869740,"name":"Saimn-I","tns":[],"alias":[]}],"name":"Test...","id":503739369,"commentId":503739369,"cp":false},{"album":{"id":2273124,"name":"Changed","cover":"http://p1.music.126.net/42rAKqZOeoW8bI2guU8ICQ==/6639950720797163.jpg"},"artists":[{"id":185503,"name":"Mario & Vidis","tns":[],"alias":[]}],"name":"Test -  (Album Redo)","id":25677136,"commentId":25677136,"cp":false},{"album":{"id":37057676,"name":"Moz Test","cover":"http://p1.music.126.net/WszbE31zLUnzLNNMYcHjYw==/109951163092588764.jpg"},"artists":[{"id":12977888,"name":"Tiki Robots","tns":[],"alias":[]}],"name":"Test","id":526044080,"commentId":526044080,"cp":false},{"album":{"id":37060015,"name":"Wav Test (Mac)","cover":"http://p1.music.126.net/xkW-b04o4SHmHzoHV_YMqg==/109951163092908940.jpg"},"artists":[{"id":12977809,"name":"Betty Fnord","tns":[],"alias":[]}],"name":"Test","id":526139184,"commentId":526139184,"cp":false},{"album":{"id":38006452,"name":"Una Minúscula Parte De Mi Existencia","cover":"http://p1.music.126.net/I4-WmO0MoQwcVkPTkjgtHw==/109951163202514092.jpg"},"artists":[{"id":13661117,"name":"Pekado","tns":[],"alias":[]}],"name":"Test","id":547168459,"commentId":547168459,"cp":false},{"album":{"id":38279375,"name":"Underground Indie Hip Hop, Vol. 1","cover":"http://p1.music.126.net/ZOTFMb63DwXeg0k526ShJg==/109951163238441259.jpg"},"artists":[{"id":13117638,"name":"Ricky Jones Band","tns":[],"alias":[]}],"name":"Test","id":551469227,"commentId":551469227,"cp":false},{"album":{"id":38295700,"name":"Animal Party Music, Vol. 2","cover":"http://p1.music.126.net/fQIM-8kjblucv-TQp6e0UQ==/109951163241353385.jpg"},"artists":[{"id":13062756,"name":"Kids Party Kings","tns":[],"alias":[]}],"name":"Test","id":551892209,"commentId":551892209,"cp":false},{"album":{"id":38527113,"name":"Pop Dance Drive , Vol. 10 (Instrumental)","cover":"http://p1.music.126.net/KFenoaJ26DwxlplNtJ8qjg==/109951163271557770.jpg"},"artists":[{"id":13106899,"name":"San Jackson Band","tns":[],"alias":[]}],"name":"Test","id":556176434,"commentId":556176434,"cp":false},{"album":{"id":48461,"name":"海猿~オリジナル・サウンドトラック","cover":"http://p1.music.126.net/26K5fAfYdrE76OcUdJc-Ug==/815837627809891.jpg"},"artists":[{"id":15325,"name":"佐藤直紀","tns":[],"alias":[]}],"name":"Test","id":510519,"commentId":510519,"cp":false},{"album":{"id":3175054,"name":"lIFE","cover":"http://p1.music.126.net/6hcCqtv5TtwjqRgW0Tbh2g==/7962663209241071.jpg"},"artists":[{"id":1087660,"name":"ANK","tns":[],"alias":[]}],"name":"Test","id":32957311,"commentId":32957311,"cp":false},{"album":{"id":37486177,"name":"Euro Dance Compilation, Vol. 1 (Special Edition)","cover":"http://p1.music.126.net/kqKGSxXwZ1sTBlLn5gpaLQ==/109951163128829565.jpg"},"artists":[{"id":13086340,"name":"Workout Jam Company","tns":[],"alias":[]}],"name":"Test","id":535005711,"commentId":535005711,"cp":false},{"album":{"id":37526534,"name":"Electro Gurus: Mixologies, Vol. 15","cover":"http://p1.music.126.net/ufu3StIjgoG01YabSTEC3Q==/109951163133140640.jpg"},"artists":[{"id":389940,"name":"Tunefunk","tns":[],"alias":[]},{"id":13077437,"name":"Deep House","tns":[],"alias":[]}],"name":"Test","id":535773517,"commentId":535773517,"cp":false},{"album":{"id":115217,"name":"Underwear","cover":"http://p1.music.126.net/FcC8JIyU1C9ydc6AcHDWUQ==/889504906914891.jpg"},"artists":[{"id":29532,"name":"Bobo Stenson","tns":[],"alias":[]}],"name":"Test","id":1111982,"commentId":1111982,"cp":false},{"album":{"id":37885169,"name":"Pressure Placement","cover":"http://p1.music.126.net/Eq_uF108M8-XfaWlG3Ft-A==/109951163177885932.jpg"},"artists":[{"id":13496416,"name":"Slaughterhouse Revolt","tns":[],"alias":[]}],"name":"Test","id":543998012,"commentId":543998012,"cp":false},{"album":{"id":137837,"name":"Devotion, Discipline, And Denial","cover":"http://p1.music.126.net/pLIHoGDK7Jq2yxNeSDuzog==/6652045348253011.jpg"},"artists":[{"id":33106,"name":"ESA","tns":[],"alias":[]}],"name":"Test","id":1339605,"commentId":1339605,"cp":false},{"album":{"id":2588386,"name":"Original Album Series","cover":"http://p1.music.126.net/-URpzNkLlwaNlTkPjJyTqg==/1253443255705942.jpg"},"artists":[{"id":66394,"name":"Ministry","tns":[],"alias":[]}],"name":"Test","id":27123965,"commentId":27123965,"cp":false},{"album":{"id":196787,"name":"Silver Sun","cover":"http://p1.music.126.net/iEPCvz1ORichsfUgA_eApQ==/919191720872382.jpg"},"artists":[{"id":43069,"name":"Silver Sun","tns":[],"alias":[]}],"name":"Test","id":1954419,"commentId":1954419,"cp":false},{"album":{"id":1947387,"name":"Little Dragon","cover":"http://p1.music.126.net/GqQvCeCntVK_CAtE4Zbv9g==/614626999937545.jpg"},"artists":[{"id":95465,"name":"Little Dragon","tns":[],"alias":[]}],"name":"Test","id":21041515,"commentId":21041515,"cp":false},{"album":{"id":36433656,"name":"Temblor","cover":"http://p1.music.126.net/c6zTuCyxyIjkl9Q5pJyLWw==/18852226369881060.jpg"},"artists":[{"id":344542,"name":"Tiko Taco","tns":[],"alias":[]}],"name":"Test","id":509377435,"commentId":509377435,"cp":false},{"album":{"id":36560599,"name":"Interview","cover":"http://p1.music.126.net/9C8kjGg1tTpQTsIFroAI2A==/18263987649554704.jpg"},"artists":[{"id":743363,"name":"Dmitry Ashin","tns":[],"alias":[]}],"name":"Test","id":513064259,"commentId":513064259,"cp":false},{"album":{"id":2711112,"name":"Body Language 13 Mixed by Azari & III","cover":"http://p1.music.126.net/f5lombJ6aZT-E-5HMJxcyg==/5775734580743512.jpg"},"artists":[{"id":83394,"name":"Unknown","tns":[],"alias":[]}],"name":"Test","id":27998356,"commentId":27998356,"cp":false}]}
         * qq : {"keyword":"test","total":393,"songs":[{"album":{"id":1290825,"name":"Smooth","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003jp3h81eT9Qn.jpg"},"artists":[{"id":1468082,"mid":"0036un4y2Vgsgq","name":"梁晓雪Kulu","title":"梁晓雪Kulu","title_hilight":"梁晓雪Kulu","type":0,"uin":0}],"name":"Test","id":"004BNnZO3rIZaT","commentId":105642629,"cp":false},{"album":{"id":1458856,"name":"Amour","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M0000045hqfV45jWbU.jpg"},"artists":[{"id":1165317,"mid":"000Oogsa22W0f7","name":"小瞳","title":"小瞳","title_hilight":"小瞳","type":0,"uin":0}],"name":"TEST","id":"004fGk2i4dD9ce","commentId":107192286,"cp":false},{"album":{"id":4931,"name":"The Mind Is A Terrible Thing To Taste","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004fA5bk1eFUMS.jpg"},"artists":[{"id":1658,"mid":"000HYOzT3e0UAH","name":"Ministry","title":"Ministry","title_hilight":"Ministry","type":2,"uin":0}],"name":"Test","id":"0017S7I23IAGCc","commentId":70311,"cp":false},{"album":{"id":283081,"name":"My Sound Is Technotronika Vol. 1","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001NrKGH0L5hEU.jpg"},"artists":[{"id":6658,"mid":"002BUZcl2OSALk","name":"Various Artists","title":"Various Artists","title_hilight":"Various Artists","type":3,"uin":0}],"name":"Test","id":"0023REhr0t5HX6","commentId":3431223,"cp":false},{"album":{"id":281707,"name":"Trance Trip","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003hh5XL4B7kFE.jpg"},"artists":[{"id":6658,"mid":"002BUZcl2OSALk","name":"Various Artists","title":"Various Artists","title_hilight":"Various Artists","type":3,"uin":0}],"name":"Test (Sol 7 Rmx)","id":"003So4r53fbM7R","commentId":3410243,"cp":false},{"album":{"id":130584,"name":"Cleansing","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003ieVow4N2hQX.jpg"},"artists":[{"id":52150,"mid":"000ddjO84DMqrM","name":"Prong","title":"Prong","title_hilight":"Prong","type":2,"uin":0}],"name":"Test","id":"001zWuLw2Sz7j4","commentId":1663915,"cp":false},{"album":{"id":650121,"name":"Original Album Classics","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003SyfWb0xZYu7.jpg"},"artists":[{"id":52150,"mid":"000ddjO84DMqrM","name":"Prong","title":"Prong","title_hilight":"Prong","type":2,"uin":0}],"name":"Test","id":"004ZzO340EWteI","commentId":7132662,"cp":false},{"album":{"id":138354,"name":"A Foolish Seperation","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001lmOOk2TsINE.jpg"},"artists":[{"id":7343,"mid":"002G6BRs2nHYpz","name":"정재욱","title":"정재욱 (郑在旭)","title_hilight":"정재욱 (郑在旭)","type":0,"uin":0}],"name":"Test","id":"002naaMO0ep7At","commentId":102818009,"cp":false},{"album":{"id":1635591,"name":"THE TIME MACHINE EP.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004cq1ag45pTrm.jpg"},"artists":[{"id":1248593,"mid":"000Xk2Gb25auxv","name":"Controller","title":"Controller","title_hilight":"Controller","type":0,"uin":0}],"name":"TEST","id":"000doGLv0JjWeh","commentId":108793304,"cp":false},{"album":{"id":1635591,"name":"THE TIME MACHINE EP.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004cq1ag45pTrm.jpg"},"artists":[{"id":1248593,"mid":"000Xk2Gb25auxv","name":"Controller","title":"Controller","title_hilight":"Controller","type":0,"uin":0}],"name":"TEST","id":"003vR4KZ1IfDTt","commentId":108793305,"cp":false},{"album":{"id":1787967,"name":"In Loving Memory","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003fEWt31xmzsk.jpg"},"artists":[{"id":225565,"mid":"004aJO2w3CNL5P","name":"Saro","title":"Saro","title_hilight":"Saro","type":0,"uin":0}],"name":"Test","id":"001oJFyB23tosE","commentId":200185612,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"Some","id":"002Sfk6p0d5pfu","commentId":3757851,"cp":false},{"album":{"id":1550416,"name":"Test","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000VBNV20xqiR5.jpg"},"artists":[{"id":1103039,"mid":"002jCDoK21WmGh","name":"HOPE-T","title":"HOPE-T","title_hilight":"HOPE-T","type":0,"uin":0}],"name":"Casual","id":"003rQqn14dqhox","commentId":107958927,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"西吧，阿西吧","id":"0022H8B30WLAf6","commentId":106867368,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"Little Thing","id":"003NS9jJ2QUV0r","commentId":3757852,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"朋友，再见","id":"0029z9tS0gHs7n","commentId":106867376,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"找见龙王，总之很难","id":"000lov4m0VQ4JQ","commentId":106867375,"cp":false},{"album":{"id":1476002,"name":"F.B.G.: The Movie (Gangsta Grillz)","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M0000010kXNu0fyUMC.jpg"},"artists":[{"id":35018,"mid":"003rLetL0AcGNH","name":"Future","title":"Future","title_hilight":"Future"},{"id":767527,"mid":"003UT7kY2d00fa","name":"Test","title":"Test","title_hilight":"<em>Test<\/em>"},{"id":18949,"mid":"000aGiRX2GUdef","name":"Sisqo","title":"Sisqo","title_hilight":"Sisqo"}],"name":"See It To Believe It","id":"002Lb7a9382q0X","commentId":107321624,"cp":false},{"album":{"id":1766705,"name":"Avenir en suspens","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001RrRRk0IYZu6.jpg"},"artists":[{"id":1311315,"mid":"001sF5rv0LKSPT","name":"Zakariens","title":"Zakariens","title_hilight":"Zakariens"},{"id":1311394,"mid":"000Yryu727d0NZ","name":"Test","title":"Test","title_hilight":"<em>Test<\/em>"},{"id":976026,"mid":"003zLYMX16cRih","name":"Reeno","title":"Reeno","title_hilight":"Reeno"},{"id":1084791,"mid":"002j5f0t48ient","name":"Aickone","title":"Aickone","title_hilight":"Aickone"}],"name":"Avec ce qu'on a","id":"002G0jEw3z8dU1","commentId":109931311,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"More Then Friends","id":"001MEpPL4Wsv5Z","commentId":210492299,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"He Touched Me","id":"002d8Smv2I5jXB","commentId":210492303,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"Show The World","id":"002Fxm0B2f38rq","commentId":210492297,"cp":false},{"album":{"id":2697779,"name":"3 Cups","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001V2Qfm3gfu76.jpg"},"artists":[{"id":1536089,"mid":"002DUFmA3T1Vrq","name":"test","title":"test","title_hilight":"<em>test<\/em>","type":0,"uin":0}],"name":"3 Cups","id":"002p3yaT3R79IB","commentId":207594839,"cp":false},{"album":{"id":87377,"name":"TEST DRIVE featuring JASON DERULO","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004PHLzA3AzERv.jpg"},"artists":[{"id":13937,"mid":"0044WixB1X6VMh","name":"赤西仁","title":"赤西仁 (Jin Akanishi)","title_hilight":"赤西仁 (Jin Akanishi)","type":0,"uin":0}],"name":"TEST DRIVE","id":"002cDlol0QtQhy","commentId":1026486,"cp":false},{"album":{"id":87377,"name":"TEST DRIVE featuring JASON DERULO","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004PHLzA3AzERv.jpg"},"artists":[{"id":13937,"mid":"0044WixB1X6VMh","name":"赤西仁","title":"赤西仁 (Jin Akanishi)","title_hilight":"赤西仁 (Jin Akanishi)","type":0,"uin":0}],"name":"TEST DRIVE","id":"000XxtwE38gnYe","commentId":1026489,"cp":false},{"album":{"id":2251222,"name":"This Is A Test (Remixes)","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000002KfOSo3m2uhu.jpg"},"artists":[{"id":9371,"mid":"0029cGzw2tZTrQ","name":"Armin Van Buuren","title":"Armin Van Buuren","title_hilight":"Armin Van Buuren","type":0,"uin":0}],"name":"This Is A Test","id":"0030Yzd821kC1Z","commentId":203939619,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"绿色大裤衩","id":"001wxR5n4DoPwH","commentId":106867367,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"朋友，你好","id":"0046aLqv2bBHbm","commentId":106867365,"cp":false},{"album":{"id":1225474,"name":"Test","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000002bzUqO3TwvQa.jpg"},"artists":[{"id":1077138,"mid":"000XT1281d9SoZ","name":"Rosper","title":"Rosper","title_hilight":"Rosper","type":0,"uin":0}],"name":"Synthetic","id":"003k2ibZ099cSS","commentId":105128588,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"At A Loss","id":"000Kpxov2Vud32","commentId":3757842,"cp":false}]}
         * xiami : {"total":5261,"songs":[{"album":{"id":497593,"name":"海猿～オリジナル・サウンドトラック","cover":"https://pic.xiami.net/images/album/img19/32319/4975931330507218_2.jpg"},"artists":[{"id":32319,"name":"佐藤直紀","avatar":"http://pic.xiami.net/images/artist/12320839104109_1.jpg"}],"name":"Test","id":1770842424,"commentId":1770842424,"cp":false},{"album":{"id":2102710027,"name":"Mike Gao - Shifty ft. Anderson Paak and Doja Cat","cover":"https://pic.xiami.net/images/album/img48/125148/2818601489281860_2.jpg"},"artists":[{"id":125148,"name":"Mike Gao","avatar":"http://pic.xiami.net/images/artistlogo/43/14892798745043_1.jpg"}],"name":"Mike Gao - Shifty ft. Anderson Paak & Doja Cat","id":1795630336,"commentId":1795630336,"cp":false},{"album":{"id":2102660539,"name":"Da Rhyizm","cover":"https://pic.xiami.net/images/album/img22/131/5847ac282dfef_6569022_1481092136_2.jpg"},"artists":[{"id":120840,"name":"Da-little","avatar":"http://pic.xiami.net/images/artistlogo/51/13518357891951_1.jpg"}],"name":"glow feat. [TEST]","id":1795318198,"commentId":1795318198,"cp":false},{"album":{"id":180757,"name":"Fast Times at Barrington High","cover":"https://pic.xiami.net/images/album/img9/26709/180757_2.jpg"},"artists":[{"id":26709,"name":"The Academy Is...","avatar":"http://pic.xiami.net/images/artist/12196331048090_1.jpg"}],"name":"Test ","id":3031975,"commentId":3031975,"cp":false},{"album":{"id":8746,"name":"Jade-1","cover":"https://pic.xiami.net/images/album/img69/1569/87461350287062_2.jpg"},"artists":[{"id":1569,"name":"关心妍","avatar":"http://pic.xiami.net/images/artistlogo/73/14359202865773_1.jpg"}],"name":"考验","id":108720,"commentId":108720,"cp":false},{"album":{"id":2102726006,"name":"WWE: The Music, Vol. 4","cover":"https://pic.xiami.net/images/album/img47/535947/15060931490535947_2.jpg"},"artists":[{"id":111645,"name":"Jim Johnston","avatar":"http://pic.xiami.net/images/artistlogo/3/13395894167103_1.png"}],"name":"This Is a Test","id":1795736633,"commentId":1795736633,"cp":false},{"album":{"id":2102669778,"name":"Test","cover":"https://pic.xiami.net/images/album/img72/105/5858b952c0d42_5290672_1482209618_2.jpg"},"artists":[{"id":2110190497,"name":"Saro","avatar":"http://pic.xiami.net/images/artistlogo/38/14822092714038_1.jpg"}],"name":"Test","id":1795405468,"commentId":1795405468,"cp":false},{"album":{"id":1928386911,"name":"Dfsda","cover":"https://pic.xiami.net/images/album/img10/878106010/19283869111428386911_2.jpg"},"artists":[{"id":878106010,"name":"我是红茶至尊官方账号","avatar":"http://pic.xiami.net/images/artistlogo/31/15258315582531_1.jpg"}],"name":"test","id":1774144132,"commentId":1774144132,"cp":false},{"album":{"id":2102669774,"name":"In Loving Memory","cover":"https://pic.xiami.net/images/album/img40/2/5858b85fe57d9_105840_1482209375_2.jpg"},"artists":[{"id":2110190497,"name":"Saro","avatar":"http://pic.xiami.net/images/artistlogo/38/14822092714038_1.jpg"}],"name":"Test","id":1795405442,"commentId":1795405442,"cp":false},{"album":{"id":469689,"name":"Portal 2 (Songs to Test By) (Volume 3)","cover":"https://pic.xiami.net/images/album/img71/98071/4696891318359739_2.jpg"},"artists":[{"id":98071,"name":"Aperture Science Psychoacoustics Laboratory","avatar":"http://pic.xiami.net/images/artistlogo/93/13182725835693_1.jpg"}],"name":"TEST","id":1770532276,"commentId":1770532276,"cp":false},{"album":{"id":1500354893,"name":"Best Of","cover":"https://pic.xiami.net/images/album/img92/354892/3548921400354892_2.jpg"},"artists":[{"id":43565,"name":"Little Dragon","avatar":"http://pic.xiami.net/images/artist/79/12601802321679_1.jpg"}],"name":"Test","id":1773196224,"commentId":1773196224,"cp":false},{"album":{"id":253590,"name":"Little Dragon","cover":"https://pic.xiami.net/images/album/img90/253590/j06237k1ybz_2.jpg"},"artists":[{"id":43565,"name":"Little Dragon","avatar":"http://pic.xiami.net/images/artist/79/12601802321679_1.jpg"}],"name":"Test","id":2904033,"commentId":2904033,"cp":false},{"album":{"id":2102760301,"name":"Test","cover":"https://pic.xiami.net/images/album/img30/297030/1928361496297030_2.jpg"},"artists":[{"id":1630974860,"name":"Gelvetta","avatar":"http://pic.xiami.net/images/artistlogo/48/14309748606548_1.jpg"}],"name":"Test","id":1795948776,"commentId":1795948776,"cp":false},{"album":{"id":126055014,"name":"Test (Original Motion Picture Soundtrack)","cover":"https://pic.xiami.net/images/album/img14/626055014/1260550141426055014_2.jpg"},"artists":[{"id":626055014,"name":"Ceiri Torjussen","avatar":"http://pic.xiami.net/images/artistlogo/36/14260550141236_1.jpg"}],"name":"Xxx Dance","id":1774055962,"commentId":1774055962,"cp":false},{"album":{"id":2100331602,"name":"测试-TEST","cover":"https://pic.xiami.net/images/album/img88/2100022988/21003316021462647732_2.jpg"},"artists":[{"id":2100022988,"name":"Yippee","avatar":"http://pic.xiami.net/images/artistlogo/47/14626997427747_1.jpg"}],"name":"朋友，再见(Bye,see u)","id":1776043572,"commentId":1776043572,"cp":false},{"album":{"id":566351813,"name":"泡泡糖test","cover":"https://pic.xiami.net/images/album/img16/166351116/5663518131366351822_2.jpg"},"artists":[{"id":166351116,"name":"大大泡泡糖","avatar":"http://pic.xiami.net/images/artistlogo/65/13663526734065_1.jpg"}],"name":"01 - Girl's Carnival (Live)","id":1771824356,"commentId":1771824356,"cp":false},{"album":{"id":400153,"name":"Devotion, Discipline, And Denial","cover":"https://pic.xiami.net/images/album/img13/72513/4001531283410212_2.jpg"},"artists":[{"id":72513,"name":"ESA","avatar":"http://pic.xiami.net/images/artist/56/12694887073356_1.jpg"}],"name":"Test","id":1769744462,"commentId":1769744462,"cp":false},{"album":{"id":430231,"name":"The House","cover":"https://pic.xiami.net/images/album/img85/87185/4302311299819472_2.jpg"},"artists":[{"id":87185,"name":"Romantic Couch","avatar":"http://pic.xiami.net/images/artistlogo/73/13014734264173_1.jpg"}],"name":"Test (Makes No Sense)","id":1770084411,"commentId":1770084411,"cp":false},{"album":{"id":278675,"name":"Underwear","cover":"https://pic.xiami.net/images/album/img7/49307/2786751237780423_2.jpg"},"artists":[{"id":49307,"name":"Bobo Stenson","avatar":"http://pic.xiami.net/images/artist/12270632508521_1.jpg"}],"name":"Test","id":3132914,"commentId":3132914,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484888,"commentId":1770484888,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484889,"commentId":1770484889,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484890,"commentId":1770484890,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484891,"commentId":1770484891,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484892,"commentId":1770484892,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484894,"commentId":1770484894,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484895,"commentId":1770484895,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484897,"commentId":1770484897,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484898,"commentId":1770484898,"cp":false},{"album":{"id":468881,"name":"Stuck In This Ocean","cover":"https://pic.xiami.net/images/album/img9/97609/4688811317286130_2.jpg"},"artists":[{"id":97609,"name":"Airship","avatar":"http://pic.xiami.net/images/artistlogo/31/13172753748831_1.jpg"}],"name":"Test","id":1770522595,"commentId":1770522595,"cp":false},{"album":{"id":1275767700,"name":"Dino Valente","cover":"https://pic.xiami.net/images/album/img72/2075755272/12757677001375767700_2.jpg"},"artists":[{"id":2075755272,"name":"Dino Valente","avatar":"http://pic.xiami.net/images/artistlogo/38/13757552726038_1.png"}],"name":"Test","id":1772076510,"commentId":1772076510,"cp":false}]}
         */

        private NeteaseBean netease;
        private QqBean qq;
        private XiamiBean xiami;

        public NeteaseBean getNetease() {
            return netease;
        }

        public void setNetease(NeteaseBean netease) {
            this.netease = netease;
        }

        public QqBean getQq() {
            return qq;
        }

        public void setQq(QqBean qq) {
            this.qq = qq;
        }

        public XiamiBean getXiami() {
            return xiami;
        }

        public void setXiami(XiamiBean xiami) {
            this.xiami = xiami;
        }

        public static class NeteaseBean {
            /**
             * total : 600
             * songs : [{"album":{"id":38534319,"name":"TEST","cover":"http://p1.music.126.net/R9Zx40i5oCv1xCnM2HBvDQ==/109951163275466550.jpg"},"artists":[{"id":13790052,"name":"AKA-趸趸","tns":[],"alias":[]},{"id":0,"name":"Junkky","tns":[],"alias":[]}],"name":"Test","id":556709500,"commentId":556709500,"cp":false},{"album":{"id":398399,"name":"Portal 2 (Songs to Test By) (Volume 3)","cover":"http://p1.music.126.net/cnDqZYGJwgaDq1TduTLZIQ==/1687750348643622.jpg"},"artists":[{"id":87366,"name":"Aperture Science Psychoacoustics Laboratory","tns":[],"alias":[]}],"name":"TEST","id":3939099,"commentId":3939099,"cp":false},{"album":{"id":38229323,"name":"Laboratory Vol.4","cover":"http://p1.music.126.net/vZrpIVEY1LsrQTy5s5Srcg==/109951163235027384.jpg"},"artists":[{"id":12070016,"name":"零","tns":[],"alias":[]}],"name":"TEST","id":550901763,"commentId":550901763,"cp":false},{"album":{"id":35947191,"name":"Bootleg","cover":"http://p1.music.126.net/T0d20qnOQsld5NYTQOZLGg==/18232101812337645.jpg"},"artists":[{"id":12603713,"name":"Quadrough","tns":[],"alias":[]}],"name":"Test","id":498367998,"commentId":498367998,"cp":false},{"album":{"id":2715661,"name":"Test","cover":"http://p1.music.126.net/-RvneWGV839m3jbKdJJr_A==/5832909185401982.jpg"},"artists":[{"id":792117,"name":"TheBlackParrot","tns":[],"alias":[]}],"name":"Test","id":28028418,"commentId":28028418,"cp":false},{"album":{"id":2591679,"name":"Test / 4ever","cover":"http://p1.music.126.net/uFmDQtzw3T-mAxOB0oQlXw==/5990139348542476.jpg"},"artists":[{"id":95465,"name":"Little Dragon","tns":[],"alias":[]}],"name":"Test","id":27225009,"commentId":27225009,"cp":false},{"album":{"id":38383187,"name":"Test","cover":"http://p1.music.126.net/ToCfzpaScTj4MgVfRyCdKA==/109951163252193194.jpg"},"artists":[{"id":13286754,"name":"杏坂","tns":[],"alias":[]}],"name":"Test","id":553660131,"commentId":553660131,"cp":false},{"album":{"id":36943039,"name":"test","cover":"http://p1.music.126.net/Gf5atu59mXUekutPAb2n5w==/109951163078985716.jpg"},"artists":[{"id":12090019,"name":"HM2","tns":[],"alias":[]}],"name":"test","id":523045073,"commentId":523045073,"cp":false},{"album":{"id":37587283,"name":"test","cover":"http://p1.music.126.net/J92RhdtS9s66djLW148U7g==/109951163141778760.jpg"},"artists":[{"id":0,"name":"Aspric","tns":[],"alias":[]}],"name":"test","id":537349922,"commentId":537349922,"cp":false},{"album":{"id":36123276,"name":"I Am the Future","cover":"http://p1.music.126.net/LqyXnBRP5ULS0tuEkLBc8A==/18300271533256014.jpg"},"artists":[{"id":12869740,"name":"Saimn-I","tns":[],"alias":[]}],"name":"Test...","id":503739369,"commentId":503739369,"cp":false},{"album":{"id":2273124,"name":"Changed","cover":"http://p1.music.126.net/42rAKqZOeoW8bI2guU8ICQ==/6639950720797163.jpg"},"artists":[{"id":185503,"name":"Mario & Vidis","tns":[],"alias":[]}],"name":"Test -  (Album Redo)","id":25677136,"commentId":25677136,"cp":false},{"album":{"id":37057676,"name":"Moz Test","cover":"http://p1.music.126.net/WszbE31zLUnzLNNMYcHjYw==/109951163092588764.jpg"},"artists":[{"id":12977888,"name":"Tiki Robots","tns":[],"alias":[]}],"name":"Test","id":526044080,"commentId":526044080,"cp":false},{"album":{"id":37060015,"name":"Wav Test (Mac)","cover":"http://p1.music.126.net/xkW-b04o4SHmHzoHV_YMqg==/109951163092908940.jpg"},"artists":[{"id":12977809,"name":"Betty Fnord","tns":[],"alias":[]}],"name":"Test","id":526139184,"commentId":526139184,"cp":false},{"album":{"id":38006452,"name":"Una Minúscula Parte De Mi Existencia","cover":"http://p1.music.126.net/I4-WmO0MoQwcVkPTkjgtHw==/109951163202514092.jpg"},"artists":[{"id":13661117,"name":"Pekado","tns":[],"alias":[]}],"name":"Test","id":547168459,"commentId":547168459,"cp":false},{"album":{"id":38279375,"name":"Underground Indie Hip Hop, Vol. 1","cover":"http://p1.music.126.net/ZOTFMb63DwXeg0k526ShJg==/109951163238441259.jpg"},"artists":[{"id":13117638,"name":"Ricky Jones Band","tns":[],"alias":[]}],"name":"Test","id":551469227,"commentId":551469227,"cp":false},{"album":{"id":38295700,"name":"Animal Party Music, Vol. 2","cover":"http://p1.music.126.net/fQIM-8kjblucv-TQp6e0UQ==/109951163241353385.jpg"},"artists":[{"id":13062756,"name":"Kids Party Kings","tns":[],"alias":[]}],"name":"Test","id":551892209,"commentId":551892209,"cp":false},{"album":{"id":38527113,"name":"Pop Dance Drive , Vol. 10 (Instrumental)","cover":"http://p1.music.126.net/KFenoaJ26DwxlplNtJ8qjg==/109951163271557770.jpg"},"artists":[{"id":13106899,"name":"San Jackson Band","tns":[],"alias":[]}],"name":"Test","id":556176434,"commentId":556176434,"cp":false},{"album":{"id":48461,"name":"海猿~オリジナル・サウンドトラック","cover":"http://p1.music.126.net/26K5fAfYdrE76OcUdJc-Ug==/815837627809891.jpg"},"artists":[{"id":15325,"name":"佐藤直紀","tns":[],"alias":[]}],"name":"Test","id":510519,"commentId":510519,"cp":false},{"album":{"id":3175054,"name":"lIFE","cover":"http://p1.music.126.net/6hcCqtv5TtwjqRgW0Tbh2g==/7962663209241071.jpg"},"artists":[{"id":1087660,"name":"ANK","tns":[],"alias":[]}],"name":"Test","id":32957311,"commentId":32957311,"cp":false},{"album":{"id":37486177,"name":"Euro Dance Compilation, Vol. 1 (Special Edition)","cover":"http://p1.music.126.net/kqKGSxXwZ1sTBlLn5gpaLQ==/109951163128829565.jpg"},"artists":[{"id":13086340,"name":"Workout Jam Company","tns":[],"alias":[]}],"name":"Test","id":535005711,"commentId":535005711,"cp":false},{"album":{"id":37526534,"name":"Electro Gurus: Mixologies, Vol. 15","cover":"http://p1.music.126.net/ufu3StIjgoG01YabSTEC3Q==/109951163133140640.jpg"},"artists":[{"id":389940,"name":"Tunefunk","tns":[],"alias":[]},{"id":13077437,"name":"Deep House","tns":[],"alias":[]}],"name":"Test","id":535773517,"commentId":535773517,"cp":false},{"album":{"id":115217,"name":"Underwear","cover":"http://p1.music.126.net/FcC8JIyU1C9ydc6AcHDWUQ==/889504906914891.jpg"},"artists":[{"id":29532,"name":"Bobo Stenson","tns":[],"alias":[]}],"name":"Test","id":1111982,"commentId":1111982,"cp":false},{"album":{"id":37885169,"name":"Pressure Placement","cover":"http://p1.music.126.net/Eq_uF108M8-XfaWlG3Ft-A==/109951163177885932.jpg"},"artists":[{"id":13496416,"name":"Slaughterhouse Revolt","tns":[],"alias":[]}],"name":"Test","id":543998012,"commentId":543998012,"cp":false},{"album":{"id":137837,"name":"Devotion, Discipline, And Denial","cover":"http://p1.music.126.net/pLIHoGDK7Jq2yxNeSDuzog==/6652045348253011.jpg"},"artists":[{"id":33106,"name":"ESA","tns":[],"alias":[]}],"name":"Test","id":1339605,"commentId":1339605,"cp":false},{"album":{"id":2588386,"name":"Original Album Series","cover":"http://p1.music.126.net/-URpzNkLlwaNlTkPjJyTqg==/1253443255705942.jpg"},"artists":[{"id":66394,"name":"Ministry","tns":[],"alias":[]}],"name":"Test","id":27123965,"commentId":27123965,"cp":false},{"album":{"id":196787,"name":"Silver Sun","cover":"http://p1.music.126.net/iEPCvz1ORichsfUgA_eApQ==/919191720872382.jpg"},"artists":[{"id":43069,"name":"Silver Sun","tns":[],"alias":[]}],"name":"Test","id":1954419,"commentId":1954419,"cp":false},{"album":{"id":1947387,"name":"Little Dragon","cover":"http://p1.music.126.net/GqQvCeCntVK_CAtE4Zbv9g==/614626999937545.jpg"},"artists":[{"id":95465,"name":"Little Dragon","tns":[],"alias":[]}],"name":"Test","id":21041515,"commentId":21041515,"cp":false},{"album":{"id":36433656,"name":"Temblor","cover":"http://p1.music.126.net/c6zTuCyxyIjkl9Q5pJyLWw==/18852226369881060.jpg"},"artists":[{"id":344542,"name":"Tiko Taco","tns":[],"alias":[]}],"name":"Test","id":509377435,"commentId":509377435,"cp":false},{"album":{"id":36560599,"name":"Interview","cover":"http://p1.music.126.net/9C8kjGg1tTpQTsIFroAI2A==/18263987649554704.jpg"},"artists":[{"id":743363,"name":"Dmitry Ashin","tns":[],"alias":[]}],"name":"Test","id":513064259,"commentId":513064259,"cp":false},{"album":{"id":2711112,"name":"Body Language 13 Mixed by Azari & III","cover":"http://p1.music.126.net/f5lombJ6aZT-E-5HMJxcyg==/5775734580743512.jpg"},"artists":[{"id":83394,"name":"Unknown","tns":[],"alias":[]}],"name":"Test","id":27998356,"commentId":27998356,"cp":false}]
             */

            private int total;
            private List<SongsBean> songs;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<SongsBean> getSongs() {
                return songs;
            }

            public void setSongs(List<SongsBean> songs) {
                this.songs = songs;
            }

            public static class SongsBean {
                /**
                 * album : {"id":38534319,"name":"TEST","cover":"http://p1.music.126.net/R9Zx40i5oCv1xCnM2HBvDQ==/109951163275466550.jpg"}
                 * artists : [{"id":13790052,"name":"AKA-趸趸","tns":[],"alias":[]},{"id":0,"name":"Junkky","tns":[],"alias":[]}]
                 * name : Test
                 * id : 556709500
                 * commentId : 556709500
                 * cp : false
                 */

                private AlbumBean album;
                private String name;
                private int id;
                private int commentId;
                private boolean cp;
                private List<ArtistsBean> artists;

                public AlbumBean getAlbum() {
                    return album;
                }

                public void setAlbum(AlbumBean album) {
                    this.album = album;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCommentId() {
                    return commentId;
                }

                public void setCommentId(int commentId) {
                    this.commentId = commentId;
                }

                public boolean isCp() {
                    return cp;
                }

                public void setCp(boolean cp) {
                    this.cp = cp;
                }

                public List<ArtistsBean> getArtists() {
                    return artists;
                }

                public void setArtists(List<ArtistsBean> artists) {
                    this.artists = artists;
                }

                public static class AlbumBean {
                    /**
                     * id : 38534319
                     * name : TEST
                     * cover : http://p1.music.126.net/R9Zx40i5oCv1xCnM2HBvDQ==/109951163275466550.jpg
                     */

                    private int id;
                    private String name;
                    private String cover;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getCover() {
                        return cover;
                    }

                    public void setCover(String cover) {
                        this.cover = cover;
                    }
                }

                public static class ArtistsBean {
                    /**
                     * id : 13790052
                     * name : AKA-趸趸
                     * tns : []
                     * alias : []
                     */

                    private int id;
                    private String name;
                    private List<?> tns;
                    private List<?> alias;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public List<?> getTns() {
                        return tns;
                    }

                    public void setTns(List<?> tns) {
                        this.tns = tns;
                    }

                    public List<?> getAlias() {
                        return alias;
                    }

                    public void setAlias(List<?> alias) {
                        this.alias = alias;
                    }
                }
            }
        }

        public static class QqBean {
            /**
             * keyword : test
             * total : 393
             * songs : [{"album":{"id":1290825,"name":"Smooth","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003jp3h81eT9Qn.jpg"},"artists":[{"id":1468082,"mid":"0036un4y2Vgsgq","name":"梁晓雪Kulu","title":"梁晓雪Kulu","title_hilight":"梁晓雪Kulu","type":0,"uin":0}],"name":"Test","id":"004BNnZO3rIZaT","commentId":105642629,"cp":false},{"album":{"id":1458856,"name":"Amour","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M0000045hqfV45jWbU.jpg"},"artists":[{"id":1165317,"mid":"000Oogsa22W0f7","name":"小瞳","title":"小瞳","title_hilight":"小瞳","type":0,"uin":0}],"name":"TEST","id":"004fGk2i4dD9ce","commentId":107192286,"cp":false},{"album":{"id":4931,"name":"The Mind Is A Terrible Thing To Taste","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004fA5bk1eFUMS.jpg"},"artists":[{"id":1658,"mid":"000HYOzT3e0UAH","name":"Ministry","title":"Ministry","title_hilight":"Ministry","type":2,"uin":0}],"name":"Test","id":"0017S7I23IAGCc","commentId":70311,"cp":false},{"album":{"id":283081,"name":"My Sound Is Technotronika Vol. 1","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001NrKGH0L5hEU.jpg"},"artists":[{"id":6658,"mid":"002BUZcl2OSALk","name":"Various Artists","title":"Various Artists","title_hilight":"Various Artists","type":3,"uin":0}],"name":"Test","id":"0023REhr0t5HX6","commentId":3431223,"cp":false},{"album":{"id":281707,"name":"Trance Trip","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003hh5XL4B7kFE.jpg"},"artists":[{"id":6658,"mid":"002BUZcl2OSALk","name":"Various Artists","title":"Various Artists","title_hilight":"Various Artists","type":3,"uin":0}],"name":"Test (Sol 7 Rmx)","id":"003So4r53fbM7R","commentId":3410243,"cp":false},{"album":{"id":130584,"name":"Cleansing","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003ieVow4N2hQX.jpg"},"artists":[{"id":52150,"mid":"000ddjO84DMqrM","name":"Prong","title":"Prong","title_hilight":"Prong","type":2,"uin":0}],"name":"Test","id":"001zWuLw2Sz7j4","commentId":1663915,"cp":false},{"album":{"id":650121,"name":"Original Album Classics","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003SyfWb0xZYu7.jpg"},"artists":[{"id":52150,"mid":"000ddjO84DMqrM","name":"Prong","title":"Prong","title_hilight":"Prong","type":2,"uin":0}],"name":"Test","id":"004ZzO340EWteI","commentId":7132662,"cp":false},{"album":{"id":138354,"name":"A Foolish Seperation","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001lmOOk2TsINE.jpg"},"artists":[{"id":7343,"mid":"002G6BRs2nHYpz","name":"정재욱","title":"정재욱 (郑在旭)","title_hilight":"정재욱 (郑在旭)","type":0,"uin":0}],"name":"Test","id":"002naaMO0ep7At","commentId":102818009,"cp":false},{"album":{"id":1635591,"name":"THE TIME MACHINE EP.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004cq1ag45pTrm.jpg"},"artists":[{"id":1248593,"mid":"000Xk2Gb25auxv","name":"Controller","title":"Controller","title_hilight":"Controller","type":0,"uin":0}],"name":"TEST","id":"000doGLv0JjWeh","commentId":108793304,"cp":false},{"album":{"id":1635591,"name":"THE TIME MACHINE EP.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004cq1ag45pTrm.jpg"},"artists":[{"id":1248593,"mid":"000Xk2Gb25auxv","name":"Controller","title":"Controller","title_hilight":"Controller","type":0,"uin":0}],"name":"TEST","id":"003vR4KZ1IfDTt","commentId":108793305,"cp":false},{"album":{"id":1787967,"name":"In Loving Memory","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003fEWt31xmzsk.jpg"},"artists":[{"id":225565,"mid":"004aJO2w3CNL5P","name":"Saro","title":"Saro","title_hilight":"Saro","type":0,"uin":0}],"name":"Test","id":"001oJFyB23tosE","commentId":200185612,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"Some","id":"002Sfk6p0d5pfu","commentId":3757851,"cp":false},{"album":{"id":1550416,"name":"Test","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000VBNV20xqiR5.jpg"},"artists":[{"id":1103039,"mid":"002jCDoK21WmGh","name":"HOPE-T","title":"HOPE-T","title_hilight":"HOPE-T","type":0,"uin":0}],"name":"Casual","id":"003rQqn14dqhox","commentId":107958927,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"西吧，阿西吧","id":"0022H8B30WLAf6","commentId":106867368,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"Little Thing","id":"003NS9jJ2QUV0r","commentId":3757852,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"朋友，再见","id":"0029z9tS0gHs7n","commentId":106867376,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"找见龙王，总之很难","id":"000lov4m0VQ4JQ","commentId":106867375,"cp":false},{"album":{"id":1476002,"name":"F.B.G.: The Movie (Gangsta Grillz)","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M0000010kXNu0fyUMC.jpg"},"artists":[{"id":35018,"mid":"003rLetL0AcGNH","name":"Future","title":"Future","title_hilight":"Future"},{"id":767527,"mid":"003UT7kY2d00fa","name":"Test","title":"Test","title_hilight":"<em>Test<\/em>"},{"id":18949,"mid":"000aGiRX2GUdef","name":"Sisqo","title":"Sisqo","title_hilight":"Sisqo"}],"name":"See It To Believe It","id":"002Lb7a9382q0X","commentId":107321624,"cp":false},{"album":{"id":1766705,"name":"Avenir en suspens","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001RrRRk0IYZu6.jpg"},"artists":[{"id":1311315,"mid":"001sF5rv0LKSPT","name":"Zakariens","title":"Zakariens","title_hilight":"Zakariens"},{"id":1311394,"mid":"000Yryu727d0NZ","name":"Test","title":"Test","title_hilight":"<em>Test<\/em>"},{"id":976026,"mid":"003zLYMX16cRih","name":"Reeno","title":"Reeno","title_hilight":"Reeno"},{"id":1084791,"mid":"002j5f0t48ient","name":"Aickone","title":"Aickone","title_hilight":"Aickone"}],"name":"Avec ce qu'on a","id":"002G0jEw3z8dU1","commentId":109931311,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"More Then Friends","id":"001MEpPL4Wsv5Z","commentId":210492299,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"He Touched Me","id":"002d8Smv2I5jXB","commentId":210492303,"cp":false},{"album":{"id":3067015,"name":"From The Ground Up","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000000nAmlX375dZs.jpg"},"artists":[{"id":1874558,"mid":"000OvQ8Q3Pi8gY","name":"T.E.S.T.","title":"T.E.S.T.","title_hilight":"T.E.S.T.","type":0,"uin":0}],"name":"Show The World","id":"002Fxm0B2f38rq","commentId":210492297,"cp":false},{"album":{"id":2697779,"name":"3 Cups","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001V2Qfm3gfu76.jpg"},"artists":[{"id":1536089,"mid":"002DUFmA3T1Vrq","name":"test","title":"test","title_hilight":"<em>test<\/em>","type":0,"uin":0}],"name":"3 Cups","id":"002p3yaT3R79IB","commentId":207594839,"cp":false},{"album":{"id":87377,"name":"TEST DRIVE featuring JASON DERULO","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004PHLzA3AzERv.jpg"},"artists":[{"id":13937,"mid":"0044WixB1X6VMh","name":"赤西仁","title":"赤西仁 (Jin Akanishi)","title_hilight":"赤西仁 (Jin Akanishi)","type":0,"uin":0}],"name":"TEST DRIVE","id":"002cDlol0QtQhy","commentId":1026486,"cp":false},{"album":{"id":87377,"name":"TEST DRIVE featuring JASON DERULO","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004PHLzA3AzERv.jpg"},"artists":[{"id":13937,"mid":"0044WixB1X6VMh","name":"赤西仁","title":"赤西仁 (Jin Akanishi)","title_hilight":"赤西仁 (Jin Akanishi)","type":0,"uin":0}],"name":"TEST DRIVE","id":"000XxtwE38gnYe","commentId":1026489,"cp":false},{"album":{"id":2251222,"name":"This Is A Test (Remixes)","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000002KfOSo3m2uhu.jpg"},"artists":[{"id":9371,"mid":"0029cGzw2tZTrQ","name":"Armin Van Buuren","title":"Armin Van Buuren","title_hilight":"Armin Van Buuren","type":0,"uin":0}],"name":"This Is A Test","id":"0030Yzd821kC1Z","commentId":203939619,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"绿色大裤衩","id":"001wxR5n4DoPwH","commentId":106867367,"cp":false},{"album":{"id":1427095,"name":"测试-TEST","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000001ZzidZ3PfjR9.jpg"},"artists":[{"id":1160705,"mid":"002IPiNu3qVmhj","name":"Yippee","title":"Yippee","title_hilight":"Yippee","type":0,"uin":0}],"name":"朋友，你好","id":"0046aLqv2bBHbm","commentId":106867365,"cp":false},{"album":{"id":1225474,"name":"Test","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000002bzUqO3TwvQa.jpg"},"artists":[{"id":1077138,"mid":"000XT1281d9SoZ","name":"Rosper","title":"Rosper","title_hilight":"Rosper","type":0,"uin":0}],"name":"Synthetic","id":"003k2ibZ099cSS","commentId":105128588,"cp":false},{"album":{"id":319294,"name":"Test.","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000004b0FR81sBxOd.jpg"},"artists":[{"id":103614,"mid":"000CzNtU4N5tOI","name":"Madcap Fool","title":"Madcap Fool","title_hilight":"Madcap Fool","type":2,"uin":0}],"name":"At A Loss","id":"000Kpxov2Vud32","commentId":3757842,"cp":false}]
             */

            private String keyword;
            private int total;
            private List<SongsBeanX> songs;

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<SongsBeanX> getSongs() {
                return songs;
            }

            public void setSongs(List<SongsBeanX> songs) {
                this.songs = songs;
            }

            public static class SongsBeanX {
                /**
                 * album : {"id":1290825,"name":"Smooth","cover":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003jp3h81eT9Qn.jpg"}
                 * artists : [{"id":1468082,"mid":"0036un4y2Vgsgq","name":"梁晓雪Kulu","title":"梁晓雪Kulu","title_hilight":"梁晓雪Kulu","type":0,"uin":0}]
                 * name : Test
                 * id : 004BNnZO3rIZaT
                 * commentId : 105642629
                 * cp : false
                 */

                private AlbumBeanX album;
                private String name;
                private String id;
                private int commentId;
                private boolean cp;
                private List<ArtistsBeanX> artists;

                public AlbumBeanX getAlbum() {
                    return album;
                }

                public void setAlbum(AlbumBeanX album) {
                    this.album = album;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public int getCommentId() {
                    return commentId;
                }

                public void setCommentId(int commentId) {
                    this.commentId = commentId;
                }

                public boolean isCp() {
                    return cp;
                }

                public void setCp(boolean cp) {
                    this.cp = cp;
                }

                public List<ArtistsBeanX> getArtists() {
                    return artists;
                }

                public void setArtists(List<ArtistsBeanX> artists) {
                    this.artists = artists;
                }

                public static class AlbumBeanX {
                    /**
                     * id : 1290825
                     * name : Smooth
                     * cover : https://y.gtimg.cn/music/photo_new/T002R300x300M000003jp3h81eT9Qn.jpg
                     */

                    private int id;
                    private String name;
                    private String cover;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getCover() {
                        return cover;
                    }

                    public void setCover(String cover) {
                        this.cover = cover;
                    }
                }

                public static class ArtistsBeanX {
                    /**
                     * id : 1468082
                     * mid : 0036un4y2Vgsgq
                     * name : 梁晓雪Kulu
                     * title : 梁晓雪Kulu
                     * title_hilight : 梁晓雪Kulu
                     * type : 0
                     * uin : 0
                     */

                    private int id;
                    private String mid;
                    private String name;
                    private String title;
                    private String title_hilight;
                    private int type;
                    private int uin;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getTitle_hilight() {
                        return title_hilight;
                    }

                    public void setTitle_hilight(String title_hilight) {
                        this.title_hilight = title_hilight;
                    }

                    public int getType() {
                        return type;
                    }

                    public void setType(int type) {
                        this.type = type;
                    }

                    public int getUin() {
                        return uin;
                    }

                    public void setUin(int uin) {
                        this.uin = uin;
                    }
                }
            }
        }

        public static class XiamiBean {
            /**
             * total : 5261
             * songs : [{"album":{"id":497593,"name":"海猿～オリジナル・サウンドトラック","cover":"https://pic.xiami.net/images/album/img19/32319/4975931330507218_2.jpg"},"artists":[{"id":32319,"name":"佐藤直紀","avatar":"http://pic.xiami.net/images/artist/12320839104109_1.jpg"}],"name":"Test","id":1770842424,"commentId":1770842424,"cp":false},{"album":{"id":2102710027,"name":"Mike Gao - Shifty ft. Anderson Paak and Doja Cat","cover":"https://pic.xiami.net/images/album/img48/125148/2818601489281860_2.jpg"},"artists":[{"id":125148,"name":"Mike Gao","avatar":"http://pic.xiami.net/images/artistlogo/43/14892798745043_1.jpg"}],"name":"Mike Gao - Shifty ft. Anderson Paak & Doja Cat","id":1795630336,"commentId":1795630336,"cp":false},{"album":{"id":2102660539,"name":"Da Rhyizm","cover":"https://pic.xiami.net/images/album/img22/131/5847ac282dfef_6569022_1481092136_2.jpg"},"artists":[{"id":120840,"name":"Da-little","avatar":"http://pic.xiami.net/images/artistlogo/51/13518357891951_1.jpg"}],"name":"glow feat. [TEST]","id":1795318198,"commentId":1795318198,"cp":false},{"album":{"id":180757,"name":"Fast Times at Barrington High","cover":"https://pic.xiami.net/images/album/img9/26709/180757_2.jpg"},"artists":[{"id":26709,"name":"The Academy Is...","avatar":"http://pic.xiami.net/images/artist/12196331048090_1.jpg"}],"name":"Test ","id":3031975,"commentId":3031975,"cp":false},{"album":{"id":8746,"name":"Jade-1","cover":"https://pic.xiami.net/images/album/img69/1569/87461350287062_2.jpg"},"artists":[{"id":1569,"name":"关心妍","avatar":"http://pic.xiami.net/images/artistlogo/73/14359202865773_1.jpg"}],"name":"考验","id":108720,"commentId":108720,"cp":false},{"album":{"id":2102726006,"name":"WWE: The Music, Vol. 4","cover":"https://pic.xiami.net/images/album/img47/535947/15060931490535947_2.jpg"},"artists":[{"id":111645,"name":"Jim Johnston","avatar":"http://pic.xiami.net/images/artistlogo/3/13395894167103_1.png"}],"name":"This Is a Test","id":1795736633,"commentId":1795736633,"cp":false},{"album":{"id":2102669778,"name":"Test","cover":"https://pic.xiami.net/images/album/img72/105/5858b952c0d42_5290672_1482209618_2.jpg"},"artists":[{"id":2110190497,"name":"Saro","avatar":"http://pic.xiami.net/images/artistlogo/38/14822092714038_1.jpg"}],"name":"Test","id":1795405468,"commentId":1795405468,"cp":false},{"album":{"id":1928386911,"name":"Dfsda","cover":"https://pic.xiami.net/images/album/img10/878106010/19283869111428386911_2.jpg"},"artists":[{"id":878106010,"name":"我是红茶至尊官方账号","avatar":"http://pic.xiami.net/images/artistlogo/31/15258315582531_1.jpg"}],"name":"test","id":1774144132,"commentId":1774144132,"cp":false},{"album":{"id":2102669774,"name":"In Loving Memory","cover":"https://pic.xiami.net/images/album/img40/2/5858b85fe57d9_105840_1482209375_2.jpg"},"artists":[{"id":2110190497,"name":"Saro","avatar":"http://pic.xiami.net/images/artistlogo/38/14822092714038_1.jpg"}],"name":"Test","id":1795405442,"commentId":1795405442,"cp":false},{"album":{"id":469689,"name":"Portal 2 (Songs to Test By) (Volume 3)","cover":"https://pic.xiami.net/images/album/img71/98071/4696891318359739_2.jpg"},"artists":[{"id":98071,"name":"Aperture Science Psychoacoustics Laboratory","avatar":"http://pic.xiami.net/images/artistlogo/93/13182725835693_1.jpg"}],"name":"TEST","id":1770532276,"commentId":1770532276,"cp":false},{"album":{"id":1500354893,"name":"Best Of","cover":"https://pic.xiami.net/images/album/img92/354892/3548921400354892_2.jpg"},"artists":[{"id":43565,"name":"Little Dragon","avatar":"http://pic.xiami.net/images/artist/79/12601802321679_1.jpg"}],"name":"Test","id":1773196224,"commentId":1773196224,"cp":false},{"album":{"id":253590,"name":"Little Dragon","cover":"https://pic.xiami.net/images/album/img90/253590/j06237k1ybz_2.jpg"},"artists":[{"id":43565,"name":"Little Dragon","avatar":"http://pic.xiami.net/images/artist/79/12601802321679_1.jpg"}],"name":"Test","id":2904033,"commentId":2904033,"cp":false},{"album":{"id":2102760301,"name":"Test","cover":"https://pic.xiami.net/images/album/img30/297030/1928361496297030_2.jpg"},"artists":[{"id":1630974860,"name":"Gelvetta","avatar":"http://pic.xiami.net/images/artistlogo/48/14309748606548_1.jpg"}],"name":"Test","id":1795948776,"commentId":1795948776,"cp":false},{"album":{"id":126055014,"name":"Test (Original Motion Picture Soundtrack)","cover":"https://pic.xiami.net/images/album/img14/626055014/1260550141426055014_2.jpg"},"artists":[{"id":626055014,"name":"Ceiri Torjussen","avatar":"http://pic.xiami.net/images/artistlogo/36/14260550141236_1.jpg"}],"name":"Xxx Dance","id":1774055962,"commentId":1774055962,"cp":false},{"album":{"id":2100331602,"name":"测试-TEST","cover":"https://pic.xiami.net/images/album/img88/2100022988/21003316021462647732_2.jpg"},"artists":[{"id":2100022988,"name":"Yippee","avatar":"http://pic.xiami.net/images/artistlogo/47/14626997427747_1.jpg"}],"name":"朋友，再见(Bye,see u)","id":1776043572,"commentId":1776043572,"cp":false},{"album":{"id":566351813,"name":"泡泡糖test","cover":"https://pic.xiami.net/images/album/img16/166351116/5663518131366351822_2.jpg"},"artists":[{"id":166351116,"name":"大大泡泡糖","avatar":"http://pic.xiami.net/images/artistlogo/65/13663526734065_1.jpg"}],"name":"01 - Girl's Carnival (Live)","id":1771824356,"commentId":1771824356,"cp":false},{"album":{"id":400153,"name":"Devotion, Discipline, And Denial","cover":"https://pic.xiami.net/images/album/img13/72513/4001531283410212_2.jpg"},"artists":[{"id":72513,"name":"ESA","avatar":"http://pic.xiami.net/images/artist/56/12694887073356_1.jpg"}],"name":"Test","id":1769744462,"commentId":1769744462,"cp":false},{"album":{"id":430231,"name":"The House","cover":"https://pic.xiami.net/images/album/img85/87185/4302311299819472_2.jpg"},"artists":[{"id":87185,"name":"Romantic Couch","avatar":"http://pic.xiami.net/images/artistlogo/73/13014734264173_1.jpg"}],"name":"Test (Makes No Sense)","id":1770084411,"commentId":1770084411,"cp":false},{"album":{"id":278675,"name":"Underwear","cover":"https://pic.xiami.net/images/album/img7/49307/2786751237780423_2.jpg"},"artists":[{"id":49307,"name":"Bobo Stenson","avatar":"http://pic.xiami.net/images/artist/12270632508521_1.jpg"}],"name":"Test","id":3132914,"commentId":3132914,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484888,"commentId":1770484888,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484889,"commentId":1770484889,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484890,"commentId":1770484890,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484891,"commentId":1770484891,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484892,"commentId":1770484892,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484894,"commentId":1770484894,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484895,"commentId":1770484895,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484897,"commentId":1770484897,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484898,"commentId":1770484898,"cp":false},{"album":{"id":468881,"name":"Stuck In This Ocean","cover":"https://pic.xiami.net/images/album/img9/97609/4688811317286130_2.jpg"},"artists":[{"id":97609,"name":"Airship","avatar":"http://pic.xiami.net/images/artistlogo/31/13172753748831_1.jpg"}],"name":"Test","id":1770522595,"commentId":1770522595,"cp":false},{"album":{"id":1275767700,"name":"Dino Valente","cover":"https://pic.xiami.net/images/album/img72/2075755272/12757677001375767700_2.jpg"},"artists":[{"id":2075755272,"name":"Dino Valente","avatar":"http://pic.xiami.net/images/artistlogo/38/13757552726038_1.png"}],"name":"Test","id":1772076510,"commentId":1772076510,"cp":false}]
             */

            private int total;
            private List<SongsBeanXX> songs;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<SongsBeanXX> getSongs() {
                return songs;
            }

            public void setSongs(List<SongsBeanXX> songs) {
                this.songs = songs;
            }

            public static class SongsBeanXX {
                /**
                 * album : {"id":497593,"name":"海猿～オリジナル・サウンドトラック","cover":"https://pic.xiami.net/images/album/img19/32319/4975931330507218_2.jpg"}
                 * artists : [{"id":32319,"name":"佐藤直紀","avatar":"http://pic.xiami.net/images/artist/12320839104109_1.jpg"}]
                 * name : Test
                 * id : 1770842424
                 * commentId : 1770842424
                 * cp : false
                 */

                private AlbumBeanXX album;
                private String name;
                private int id;
                private int commentId;
                private boolean cp;
                private List<ArtistsBeanXX> artists;

                public AlbumBeanXX getAlbum() {
                    return album;
                }

                public void setAlbum(AlbumBeanXX album) {
                    this.album = album;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCommentId() {
                    return commentId;
                }

                public void setCommentId(int commentId) {
                    this.commentId = commentId;
                }

                public boolean isCp() {
                    return cp;
                }

                public void setCp(boolean cp) {
                    this.cp = cp;
                }

                public List<ArtistsBeanXX> getArtists() {
                    return artists;
                }

                public void setArtists(List<ArtistsBeanXX> artists) {
                    this.artists = artists;
                }

                public static class AlbumBeanXX {
                    /**
                     * id : 497593
                     * name : 海猿～オリジナル・サウンドトラック
                     * cover : https://pic.xiami.net/images/album/img19/32319/4975931330507218_2.jpg
                     */

                    private int id;
                    private String name;
                    private String cover;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getCover() {
                        return cover;
                    }

                    public void setCover(String cover) {
                        this.cover = cover;
                    }
                }

                public static class ArtistsBeanXX {
                    /**
                     * id : 32319
                     * name : 佐藤直紀
                     * avatar : http://pic.xiami.net/images/artist/12320839104109_1.jpg
                     */

                    private int id;
                    private String name;
                    private String avatar;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getAvatar() {
                        return avatar;
                    }

                    public void setAvatar(String avatar) {
                        this.avatar = avatar;
                    }
                }
            }
        }
    }
}
