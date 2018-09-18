/*
 * @(#)SpringDataRedisCache.java	2018年9月18日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package com.github.javaclub.redis.operation.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.javaclub.redis.operation.RedisCache;
import com.github.javaclub.sword.util.ByteUtil;

/**
 * SpringDataRedisCache
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: SpringDataRedisCache.java 2018年9月18日 9:41:21 Exp $
 */
public class SpringDataRedisCache implements RedisCache {

	private RedisTemplate<String, String> redisTemplate;

	private static final Logger log = LoggerFactory.getLogger(SpringDataRedisCache.class);

	@Override
	public boolean put(final String key, Object object) {
		final byte[] data = ByteUtil.object2ByteArray(object);
		try {
			boolean suc = redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {

					try {
						connection.set(key.getBytes(), data);
						return true;
					} catch (Exception e) {
						log.error("", e);
					}

					return false;
				}
			});

			return suc;

		} catch (Exception e) {
			log.error("", e);
		}

		return false;
	}

	@Override
	public boolean put(final String key, Object object, final long expire) {
		final byte[] data = ByteUtil.object2ByteArray(object);
		try {
			boolean suc = redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {

					try {
						connection.setEx(key.getBytes(), expire, data);
						return true;
					} catch (Exception e) {
						log.error("", e);
					}

					return false;
				}
			});

			return suc;

		} catch (Exception e) {
			log.error("", e);
		}

		return false;

	}

	@Override
	public Object get(final String key) {
		Object data = null;
		try {
			data = redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] bb = connection.get(key.getBytes());
					if (bb != null) {
						return ByteUtil.byteArray2Object(bb);
					}

					return null;
				}
			});

		} catch (Exception e) {
			log.error("", e);
		}

		return data;
	}

	@Override
	public void setObject(final String key, Object object) {

		final String data = JSON.toJSONString(object);

		try {
			redisTemplate.execute(new RedisCallback<Integer>() {

				@Override
				public Integer doInRedis(RedisConnection connection) throws DataAccessException {

					try {
						connection.set(key.getBytes(), data.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						log.error("", e);
					}
					return 0;
				}
			});
		} catch (Exception e) {
			log.error("", e);
		}

	}

	@Override
	public <T> T getObject(final String key, Class<T> clazz) {

		String data = null;
		try {
			data = redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] bb = connection.get(key.getBytes());

					if (bb != null)
						try {
							return new String(bb, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							log.error("", e);
						}
					return null;
				}
			});

			if (data != null) {
				return JSON.parseObject(data, clazz);
			}
		} catch (Exception e) {
			log.error("", e);
			return null;
		}

		return null;
	}

	public <T> T getObject(final String key, TypeReference<T> typeReference) {

		String data;
		try {
			data = redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] bb = connection.get(key.getBytes());

					if (bb != null)
						try {
							return new String(bb, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							log.error("", e);
						}
					return null;
				}
			});

			if (data != null) {
				return JSON.parseObject(data, typeReference);
			}
		} catch (Exception e) {
			log.error("", e);
			return null;
		}

		return null;
	}

	@Override
	public void setObject(final String key, Object object, final long expire) {
		final String data = JSON.toJSONString(object);

		try {
			redisTemplate.execute(new RedisCallback<Integer>() {

				@Override
				public Integer doInRedis(RedisConnection connection) throws DataAccessException {
					try {
						connection.setEx(key.getBytes(), expire, data.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						log.error("", e);
					}
					return 0;
				}
			});
		} catch (Exception e) {
			log.error("", e);
		}

	}

	@Override
	public void setString(final String key, final String object) {

		try {
			redisTemplate.execute(new RedisCallback<Integer>() {

				@Override
				public Integer doInRedis(RedisConnection connection) throws DataAccessException {

					try {
						connection.set(key.getBytes(), object.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						log.error("", e);
					}
					return 0;
				}
			});
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public void setLong(final String key, final long object) {

		try {
			redisTemplate.execute(new RedisCallback<Integer>() {

				@Override
				public Integer doInRedis(RedisConnection connection) throws DataAccessException {

					connection.set(key.getBytes(), (object + "").getBytes());
					return 0;
				}
			});
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public String getString(final String key) {
		try {
			String data = redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] bb = connection.get(key.getBytes());

					if (bb != null)
						try {
							return new String(bb, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							log.error("", e);
						}
					return null;
				}
			});
			return data;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	@Override
	public long inc(final String key) {

		try {
			return redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.incr(key.getBytes());
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return -1L;
		}

	}

	public long inc(final String key, final long value) {

		try {
			return redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.incrBy(key.getBytes(), value);
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return -1L;
		}

	}

	@Override
	public long getLong(final String key) {
		try {
			return redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] b = connection.get(key.getBytes());

					if (b == null) {
						return -1l;
					}

					return Long.parseLong(new String(b));
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return -1L;
		}
	}

	@Override
	public boolean expire(final String key, final long seconds) {
		try {
			return redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.expire(key.getBytes(), seconds);
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

	@Override
	public long decr(final String key) {

		try {
			return redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.decr(key.getBytes());
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return -1L;
		}
	}

	@Override
	public long decr(final String key, final long value) {

		try {
			return redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.decrBy(key.getBytes(), value);
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return -1L;
		}
	}

	@Override
	public void del(final String key) {
		try {
			redisTemplate.execute(new RedisCallback<Integer>() {

				@Override
				public Integer doInRedis(RedisConnection connection) throws DataAccessException {
					connection.del(key.getBytes());
					return 0;
				}
			});
		} catch (Exception e) {
			log.error("", e);
		}

	}

	@Override
	public void del(final List<String> list) {
		try {
			redisTemplate.execute(new RedisCallback<Integer>() {

				@Override
				public Integer doInRedis(RedisConnection connection) throws DataAccessException {
					byte[][] keys = new byte[list.size()][];
					for (int i = 0; i < list.size(); i++) {
						keys[i] = list.get(i).getBytes();
					}
					connection.del(keys);
					return 0;
				}
			});
		} catch (Exception e) {
			log.error("", e);
		}

	}

	/**
	 * 批量获取
	 * 
	 * @param list
	 * @return
	 */
	public Map<String, Long> batchGetLong(final List<String> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
			
		try {
			return redisTemplate.execute(new RedisCallback<Map<String, Long>>() {

				@Override
				public Map<String, Long> doInRedis(RedisConnection connection) throws DataAccessException {

					byte[][] keys = new byte[list.size()][];
					for (int i = 0; i < list.size(); i++) {
						keys[i] = list.get(i).getBytes();
					}

					List<byte[]> datalist = connection.mGet(keys);

					Map<String, Long> map = new HashMap<String, Long>();

					for (int i = 0; i < datalist.size(); i++) {
						byte[] data = datalist.get(i);
						byte[] key = keys[i];
						if (data != null) {
							try {
								map.put(new String(key), Long.parseLong(new String(data)));
							} catch (NumberFormatException e) {
								map.put(new String(key), null);
							}
						} else
							map.put(new String(key), null);
					}

					return map;
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return null;
		}

	}

	@Override
	public boolean exists(final String key) {

		try {
			return redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.exists(key.getBytes());
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Long sadd(final String key, Object... objects) {
		final byte[][] valuses = new byte[objects.length][];
		for (int i = 0; i < objects.length; i++) {
			String data = JSON.toJSONString(objects[i]);
			valuses[i] = data.getBytes();
		}
		try {
			return redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.sAdd(key.getBytes(), valuses);
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return -1L;
		}
	}

	@Override
	public <T> List<T> getSmembers(final String key, final Class<T> clazz) {
		List<T> data = null;
		try {
			data = redisTemplate.execute(new RedisCallback<List<T>>() {

				@Override
				public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
					Set<byte[]> bb = connection.sMembers(key.getBytes());

					List<T> list = new ArrayList<>(bb.size());

					String tempStr = null;
					for (byte[] b : bb) {
						if (b != null)
							try {
								tempStr = new String(b, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								log.error("", e);
							}

						if (tempStr != null) {
							list.add(JSON.parseObject(tempStr, clazz));
						}
					}
					return list;
				}
			});

		} catch (Exception e) {
			log.error("", e);
			return null;
		}
		return data;
	}

	@Override
	public <T> T sPop(final String key, Class<T> clazz) {
		String data = null;
		try {
			data = redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] bb = connection.sPop(key.getBytes());

					if (bb != null)
						try {
							return new String(bb, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							log.error("", e);
						}
					return null;
				}
			});

			if (data != null) {
				return JSON.parseObject(data, clazz);
			}
		} catch (Exception e) {
			log.error("", e);
			return null;
		}

		return null;
	}

	@Override
	public <T> Long sRem(final String key, List<T> objects) {
		final byte[][] valuses = new byte[objects.size()][];
		for (int i = 0; i < objects.size(); i++) {
			String data = JSON.toJSONString(objects.get(i));
			valuses[i] = data.getBytes();
		}
		try {
			return redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.sRem(key.getBytes(), valuses);
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return -1L;
		}
	}

	@Override
	public Boolean setObjectIfNotExists(final String key, final Object obj, final long expire) {
		try {
			Boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					byte keybytes[] = key.getBytes();
					final String data = JSON.toJSONString(obj);
					Boolean suc;
					try {
						suc = connection.setNX(keybytes, data.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
					connection.expire(keybytes, expire);
					return suc;
				}
			});

			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Boolean setIfNotExists(final String key, final long expire) {
		Boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keybytes = key.getBytes();
				Boolean suc = connection.setNX(keybytes, String.valueOf(System.currentTimeMillis()).getBytes());
				if (!suc) {
					// 解决setNX成功，但expire没成功的问题，del后依赖下次重试
					Long oldTime = Long.parseLong(new String(connection.get(keybytes)));
					if (System.currentTimeMillis() - oldTime > expire * 1000) {
						connection.del(keybytes);
					}
				} else {
					connection.expire(keybytes, expire);
				}
				return suc;
			}
		});
		return ret;
	}

	public void pubMessage(String channel, Object message) {
		try {
			redisTemplate.convertAndSend(channel, message);
		} catch (Exception e) {
			log.error("", e);
		}
		log.warn("channel:" + channel + ",message:{}" + message);
	}

}
