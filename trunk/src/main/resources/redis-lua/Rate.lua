local num = tonumber(ARGV[2]) or 1
local current = redis.call("incr",KEYS[1])
local time = tonumber(ARGV[1])
if current==1 then
    redis.call("expire",KEYS[1],time)
    return 1
elseif current-1 < num then
    return 1
else
    return 0
end