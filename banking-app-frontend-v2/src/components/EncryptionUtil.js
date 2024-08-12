import CryptoJS from "crypto-js";

export function encryptPassword(password) {
  const key = CryptoJS.enc.Utf8.parse(process.env.REACT_APP_SECRET_KEY);

  const iv = CryptoJS.lib.WordArray.random(16);

  console.log(process.env.REACT_APP_SECRET_KEY);

  const encrypted = CryptoJS.AES.encrypt(password, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  });

  return `${iv.toString(CryptoJS.enc.Hex)}:${encrypted.toString()}`;
}
