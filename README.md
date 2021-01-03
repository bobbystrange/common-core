# common-core

a common and useful pure java library

### Feature

- common utils
- data structure(such as B-tree)
- design pattern(chain,creator,factory...)
- reflect
- POJO Ops and Bean expression
- encrypt/decrypt and encode/decode
- CLI arguments parser
- captcha
- snowflake
- text parser(JSON,Yaml,INI,CSV...)

### crypto algorithm

<table>
       <thead>
           <tr>
               <th>Algorithm</th>
               <th>Key Size</th>
               <th>Default Key Size</th>
               <th>Work Mode</th>
               <th>Padding Mode</th>
           </tr>
       </thead>
       <tbody>
           <tr>
               <td>DES</td>
               <td>56</td>
               <td>56</td>
               <td>ECB、CBC、PCBC、CTR、CTS、CFB、CFB8-CFB128、OFB、OFB8-OFB128</td>
               <td>NoPadding、PKCS5Padding、ISO10126Padding</td>
           </tr>
           <tr>
               <td>3DES</td>
               <td>112、168</td>
               <td>168</td>
               <td>ECB、CBC、PCBC、CTR、CTS、CFB、CFB8-CFB128、OFB、OFB8-OFB128</td>
               <td>NoPadding、PKCS5Padding、ISO10126Padding</td>
           </tr>
           <tr>
               <td>AES</td>
               <td>128、192、256</td>
               <td>128</td>
               <td>ECB、CBC、PCBC、CTR、CTS、CFB、CFB8-CFB128、OFB、OFB8-OFB128</td>
               <td>NoPadding、PKCS5Padding、ISO10126Padding</td>
           </tr>
       </tbody>
  </table>
