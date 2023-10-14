package io.circul.capillary

class ResponseReceiver<RES : PortResponse>(val response: RES) {

    var handled = false

    inline fun <reified T : RES> expects(
        block: (ResponseReceiver<T>) -> Unit
    ) {
        if (response is T) {
            block(ResponseReceiver(response))
            handled = true
        }
    }

    inline fun otherwise(block: (ResponseReceiver<RES>) -> Unit) {
        if (!handled) {
            block(ResponseReceiver(response))
            handled = true
        }
    }
}
