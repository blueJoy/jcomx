
import com.example.context.Context


/**
 * Created by baixiangzhu on 2017/7/26.
 */
/**
 * 测试
 * @param data   上次返回的数据
 * @param context  请求上下文
 * @param ref  ref 是要处理的字段
 */
def callback(Context context,Object data,Map ref){

    println("callback execute!")

    data["groovy"] = "test"

    println(ref)

    //可以操作redis

    def redisSource = context.sourceBaseFactory.create("redis-cluster")

    def redisClient = redisSource.jComxCache

    def key = "groovy:test:key"
    def value = "{'aa':11,'bb':'nice'}"

    redisClient.set(key,value)

    def redisVal = redisClient.get(key)

    ref["redis-test"] = redisVal

}
