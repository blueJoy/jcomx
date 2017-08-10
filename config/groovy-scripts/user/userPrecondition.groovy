
import com.example.context.Context

/**
 * Created by baixiangzhu on 2017/8/10.
 */
/**
 * 校验前置条件是否满足
 * @param Object
 * @param context
 * @param ref
 */
def callback(Context context,Object data,Map ref){

    def requestData = context.getRequest().getData()
    if(requestData == null || requestData.isEmpty())
        return false

    return true

}
