local bindings = {
    backToGame = function() --[[<...>]]  end,
    scrollUp   = function() --[[<...>]] end,
    scrollDown = function() --[[<...>]] end,
    select     = function() --[[<...>]] end,
}

local keys = {
    escape     = "backToGame",
    up         = "scrollUp",
    down       = "scrollDown",
    ["return"] = "select", -- return is a keyword that's why it has to be written like this
}
local keysReleased = {}

local buttons = {
    back = "backToGame",
    up   = "scrollUp",
    down = "scrollDown",
    a    = "select",
}
local buttonsReleased = {}

function inputHandler( input )
    local action = bindings[input]
    if action then  return action()  end
end

function love.keypressed( k )
    local binding = keys[k]
    return inputHandler( binding )
end
function love.keyreleased( k )
    local binding = keysReleased[k]
    print("a")
    return inputHandler( binding )
end


