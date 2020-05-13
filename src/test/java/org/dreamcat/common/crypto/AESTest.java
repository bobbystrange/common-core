package org.dreamcat.common.crypto;

import org.dreamcat.common.util.ByteUtil;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import javax.crypto.BadPaddingException;

import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/5/4
 */
public class AESTest {
    @Test
    public void aes() throws Exception {
        String data = "a5e80b35cc8aadee";
        byte[] key = data.getBytes();

        for (int i = 1; i < 1000; i++) {
            String input = RandomUtil.choose72(i);
            println("input    \t", input);
            byte[] en = CipherAlgorithm.AES.encryptCbc(input.getBytes(), key);
            byte[] de = CipherAlgorithm.AES.decryptCbc(en, key);
            println("decrypted\t", new String(de));
        }
    }

    @Test(expected = BadPaddingException.class)
    public void decryptStr() throws Exception {
        byte[] key = "a5e80b35cc8aadee".getBytes();
        String s = "a1e4ac75d6ef5e353c5935d5f41d9d35aa05309f4675ffd6f2120526325b892184959803ee511f22b7aabb9801fe37f12449daf9bf044c904e384a2fdcc0464f2ad209a7c6919f7f4fb832f2453cc141963c687ef1bcd26a865b2a9168e4809634f9494ee92517dd0449e84f26c78c45348d2a17c4b1fe52420b15e3a7fefa584ff390633b71512547b83aaca0cc1a235421b1371ee5f69995c6f95d188971f405e79213262c8c02bb118a9c3e591f9b655ffc0aebceb9d3c1ca03f1723acc9482c1f228a764efa0418a9aa62b7274c9e4bd5a43e7e5e1c582400c26858092ee636ce72544e29b3e62a97675dd9678d8b6ea13f72b533216436f9aabe3175811b86b4fcd4a44da195cbb1932d08b9e57cf8f1a50e20e9beb307a19fc2d31dbe1ac00411c19c7357cae223c30df61badefc099ba42d44cfee396abf6b0458f466d54c7c0493c2fe83c8eef8d2d73ffa83aeb27f4d60f19a9f8a524809ee64d96be07ed9f155763a07db539844f1e3efeebc1bf91c1886c142fb4db030095c4073aed212886e7cd51991c161c53b391b5edf3b892f7af3193afb191c7cdf6feeb58f30fa1e926f33e9696d3e59e667ca4c0aefe0e95135382e1025a36dd22c466f39fff36983735e188e6a994cb4e9bf975354698dd6778a8872186d197c8c05c1b3b6c92cf2a33e1e6618c7db731046893a27e7136570778cd8c5b9ed60f363fdf1737b97c8f677e48c778818cb50f7ce01fda04ef7f53bfcc684f788737d7fd00b1344296446f35985c67342cbb84e4e73a8da5f63553a4d6f7bc7d92cd3e4fdb30d5b5e45cd636644d8f2a0247d65185910a091da79b5f977894d28490468fbfa4592a139b3791853276f0a2284a6c764f44d6ebcf6db28d84bbbd285e876a74ae5af68a6ce465e84d77f2188e5797fa77456d5f151c5523cb551ff43008beb5361e102c4b83ea300fece7fe599d929529dc103924c64d8271171f84960c239eefab8630feac5cffe0bde357d0b980019392559877e95cd4eceee7663c15f483732544f1def73cf9dad4004e0489c97af8ef647528afec9495e7976231207e3b949fd64148fc5dc89ad3a484a9161658b0ff14eb6318e29e819ebf73a61125bb27c66b6a74ef42602b1e6eddf54eb3cb58f66ba0424277700035d09b94914ee01443c72ec37945fb3761e283abb21caa648e366628d4b1bbe745a0622736145efa1f3909437dc1df4bf0bc896dda916bb37e9ec448e90e6efcb42cbbfe3f381d74fd518c7e815b0a4d6d045183c7e3fd2c643ced17e9c0783c53c8337d9f89099edef98a2ba620d1a99d1109678fa567c9ef206e7bd4034f6eff9bd2846143051eb1e9c092cf21c42d6610b0fc63a35c2f9fecc554c799b4d9d0abafdb3bbaea60fa0c3ac7fdcbee721f35b8017c2d46875047bca5eed3e8b753a5e7efcc2b9f2f79ee275705fb0e438d1964782838cf2d3c1206494a2580c3a66f2b014bed5d9370c404fdbe98230b3afce2972cd09ad92c625461420d293ca8c14e3801360fa0b6d93892a193501d6b2f757d6ef4aa6467e59bf050f02127254cad43337e0e0145cd8e4a55ef58843451f562326ed76d3f95f69d5979573655efd964083727e27548d4f7d52e16752e9a42891c9ef253e0b2e94f5eb81b173c0c05a62def29c9eff4c4968b82c3fa065de9058e09e32db664a7df91eb53d831abc7a9b0de3b482df737bccecda7e72072ccba7a70c0fb8ca139fb195649fbaec1ce9435670175f1a6a7efbb49fba0b95ee6f73530a0e5e6024776775f0556453f0f78d4b542b9261873c438739f212d6536ad030ab6d9443938a73c927fc55a6e6d3ad5922ce3c99756649520045924ddfb5562dfcffa2bb620c40cd118900e5f6de6be9c64a415c9f9c76ac0fb86ac315c2bb09019233c071831e9b49e9d8cb8838ff9746ab7d7163a55b767e6620fbf4c1b08c2131f9dcf41e9864982079cdcfd29beb87dbc55365acd6e661e29962ab57f5f8c801261d9e4f9e9259681bb95e596243c50e2df6bd9b7efebdb342180050abf47da3b9982fbae8f313e4b5427de4e8443574754f30a42465e474ae9adcb7c5ffcf2a15edb5e8a4b9c7b179cfd17d1ff10cf5bd31118d6fbad16ed750cb766c98afd47ad70800b4f2db1dc43ee69355e7ce12ea4cf3fd21f1dffb77a1abecb71a42d0880c3f58fdc47f8b64c4e01ba99957ec98b28cbfb2f903e9c7758b81c6ab1d64a4ec7cfad1ee3d8637a2ac721ed875081a1ecfadd162bdbab0b79d9057247a428e7d5f04c22a0f4e0d153499a18b9e91cac7aa57858de5a774407b643f6597c82a3004ff313c2cd960ee0b25bd2a2db689ee1ffd5be57a8a51aa1c1646c051191cc98a2b28309adf13f2d6b7b1316a2f219b1d2ab8e0fe93bfb4a98ba99fe47c98b27ab33413455d7654e06a1a21f05c8a268ec670d3215a8d3b8fb6b72ccd08a11262c0690ba863c95fe8685c0e80d9f75b4aa79882f6ade934d1f7174acdcc9b09136dbb15a5ced920fff3673916";
        byte[] de = CipherAlgorithm.AES.decryptEcb(ByteUtil.unhex(s), key);
        println("decrypt\t", de.length, ByteUtil.hex(de));

        s = "777075e4e010b8143193342af15fa4064a9a18b284faca50a212cda6f816b9e905368bf43bb5e7ed274a031ccffc74f952e9820c6d75044e5c0d165b1e2e0ddc0538e5e70b40738ff2a03d25f5e36d68dd9f3cf4685e5e63d061aac30665e293b20fa2a543e75dd02f4f6288d7638a72fecceaeefb0b8f1eddc7f9f974824ea9fdd53c44190f1e7d8477891128c3ff37def3c293a323432dd03b623f57c1f54caec0abe4be2fcdc95f99f9487493b41cfcb3e33d3226d1f6f9d98835a70e67fcc16ef78f8f6e9eb48152feb5409e47becf72ec0fdc09460d57613bbe3adaeb28982d03e5402a7ba441e05e0ea080ede497dcc5e2885a1ebc1f9d64100b1aef6cdf71874b490481750e51fba4b1de7c3f22b81b2a9c51108fe93d619873ec4bc515d9c0cae4e82b696ca2ba80647d91ec4e282350a4780c933044e8cefc6934c2814dd39da29ce13923ccb627967b01d73dc6b6a53f6708520ea862692a9e7acb7fed606abf97abaa9bc335aa0b20bd98f3efc9d53cf7555e9eb5958ad2b0d0ca3dae98be8d41cc63dd5147959d89e79bbce89aeedcba161cc024f5b22909ad9f8dce84ec3806922505344ae47bbd108dcd16e9de31421d39b5f0baf85c9589bfd93d5cca3a925f003ee4cf09869dcd2d4af108fcfc052be8ba765546b756e6f13e2e5f0a5e6539f1b497ce9714f9881cb1f440b3116704b2c704233b764567c4d8cb5f7bf7c539adff5d93c25147a1c3e37657ebdc912488714727fc283a9c6b1e8c26ae02f1b60389b2adcfb1ea29426681830bbd18de51613864230df581387ed7f069fe51c7bff502d65867eeea3508bfc48de5a49e12e9adc93eb82ffc719ff5144892958f787a8aa13131c136b53751628f37ebb79e13d49e2e75ab54f723c7b44829895a8f075c2159078b551670931042ec0e7056618f9e57423055af11b8c49f4ebb0f75a748d6e037357a795831046d6fe80be4fdddd63219d7d3fdd7f712812df5fdbba227904292a07669c31f97e6c15622924abb5aa3984a43f17f52e222dde01847a45b81cefa0b3886bc45337eeae59cd84ca13047606b2cf935e21006eaaf1033596626ff0007c42007cb1c184063a00acb67796bad365414e24fd72581c5d62277de807b87c656fb71c773151fb2974b1385ad5f0e4ae80b513350e3279431baf9c5633f2f48e168f19d18e7970484437778f516b8502ed48f1a0d85135f32e52b4198bd0e7ad6ce00eb93ad9868921337f9d641c5400794f51ccc2f03d857b98e8be66be299caaef27cd5e1d77a62e41f8408a1fe34a803611cccc82314414b2accab3bbd65ce313ef3594a90c0dcdef3fbecb337dd19442d438da4e2bfd1eef33f93fb74f09b01d041c8c30d8aba1cd53316138b19d03ac8a14584a9736786a9ead2e683a234f0e018e85379ba0ffe3bad4e296938c1c1ac938872f2e78078e3fee7e80a9be1c169e6c43816f309932d238f6f21e355ba0bbb0369e9a66c717f0d88fd76e63eb8cecf5e5ce0968f996263d27cca1f21ddd7b24473a82c91ece2c534df2f2bd7e66979efd928fcb2668ed8ec568e0be59a197603ed98e42ced32d59e103f8f5952210e8e1c3a1501f6dfc1f79aa5e4b55c5e692a1da90afe905f67632358b77ba2ade01e381c521df9c17bd856ffddb3748bc040818be16cfb9b7296c6c949e09f4fb329d74916782b9ecf382324ecd2cebf9ef9caac836180ecabebd71f8c076f5d9ba184bc99965ac964607a5346c6409818764ab8c8c1a399d6eb4a967b5a7dd066423273e940625906e6d05a33284455dea1738a9232c31f8a9b1f069a63751c0b33f34d5f7a6c7b50a98394acde36bca74994e1edb7bce53e99d39a21f4a59ee126b70ab6d8ebad1a40899bee429b0dcc73b2aab43431c550c200812c36b195a6d91c7c289228a7fb90b03f490290772c6ea4613a3b84c19cf08e4044d3454bccc37140f8017a9b6f5d59578c4260635c04f8120ce645bf3e1bf6a30312ec318c50098cbb360e5ec0a74cc70593cc5beb7eb32e32a59872a9a8350939df0e28941eaa9a3c417101c301f0011ec914b5bb95133d32c508e5ebcd1eff5917862204c2ac692bcd41ac181c2c7033ae78b5456a5b415fc9bfa78dcc1badc72b604197cd71a95ff3da92d47125fcdd7c8da124712e3e22c0e9f353769ee57a54c6f32adb76172339118396602d807ba21cd113dfa5b5613995b9c263619e432dd30e4a2e4910474b08759377807a7acbd9c9b8e03dc37c7df03ba5d73fe47d8a7aedae81f2b3c2f574db4460fa58940ce22575aefbd05cac3c073ac51d5f5ca5e121661d93047f6eca477773c320a698c320971945f23454d95fbc8bcd619bf65190adf63b3be0228144ff8d2dee2ad59f6f01ac336fb185e2ae17ad91e96b3f752e23f9ad4f69f0e55c067d0946b1d7f1b8a7160950622e7c565b6c3e0ce4c6ca7f3c8d060d8e0af121572b349a8125c2eeef010d8dd609646c88901251b9bbd62ad69442eae4b31fbd2c5cbbb15989b0";
        de = CipherAlgorithm.AES.decryptEcb(ByteUtil.unhex(s), key);
        println("decrypt\t", de.length, ByteUtil.hex(de));
    }
}