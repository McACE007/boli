local auction_key = KEYS[1]
local incoming_bid = tonumber(ARGV[1])
local bidder_id = ARGV[2]

-- 1. Check if auction is LIVE
local status = redis.call('HGET', auction_key, 'status')
if status ~= 'LIVE' then
    return 'NOT_LIVE'
end

-- 2. Get rules
local starting_price = tonumber(redis.call('HGET', auction_key, 'startingPrice'))
local min_increment = tonumber(redis.call('HGET', auction_key, 'minIncrement'))
local current_highest = redis.call('HGET', auction_key, 'highestBid')

-- 3. Validate Bid
if not current_highest then
    -- First bid
    if incoming_bid < starting_price then
        return 'LOW_STARTING_PRICE'
    end
else
    -- Subsequent bids
    current_highest = tonumber(current_highest)
    if incoming_bid < (current_highest + min_increment) then
        return 'LOW_BID'
    end
end

-- 4. Validation passed! Update Redis.
redis.call('HMSET', auction_key, 'highestBid', incoming_bid, 'highestBidderId', bidder_id)
return 'SUCCESS'