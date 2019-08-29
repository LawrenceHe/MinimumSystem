#数据库中添加gesture列在password后面，并删除所有password为空字符串的行
ALTER TABLE users ADD gesture VARCHAR(100) AFTER password;
DELETE FROM users WHERE password='';
