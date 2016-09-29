let s:p_dir = expand('<sfile>:p:h')
let g:is_running = 0
let g:channel = -1

function! StartIfNotRunning()
    if g:is_running == 0
        echo 'starting plugin...'
        "TODO - This is a dirty hack. We should launch things without changing
        "the working directory.
        exec ':cd ' . s:p_dir
        let g:channel = rpcstart('lein', ['run'])
        let g:is_running = 1
    endif
endfunction

function! Connect()
    "call StartIfNotRunning()
    let res = rpcrequest(1, 'connect', [])
    return res
endfunction
command! Connect call Connect()

function! EvalBuffer()
    "call StartIfNotRunning()
    let res = rpcrequest(1, 'eval-buffer', [])
    return res
endfunction
command! EvalBuffer call EvalBuffer()

function! EvalCode()
    "call StartIfNotRunning()
    let res = rpcrequest(g:channel, 'eval-code', [])
    return res
endfunction
command! EvalCode call EvalCode()

function! ReplLog()
    "call StartIfNotRunning()
    let res = rpcrequest(1, 'show-log', [])
    return res
endfunction
command! ReplLog call ReplLog()

echo 'socket repl plugin loaded!'
