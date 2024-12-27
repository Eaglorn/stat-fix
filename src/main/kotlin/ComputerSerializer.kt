import com.google.gson.*
import java.lang.reflect.Type
import java.time.format.DateTimeFormatter

internal class ComputerSerializer : JsonSerializer<Computer> {
    override fun serialize(computer: Computer, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", computer.name)
        jsonObject.addProperty("ekp", computer.ekp)
        jsonObject.addProperty("version", computer.version)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        jsonObject.addProperty("dateChange", formatter.format(computer.dateChange))
        val operationsArray = JsonArray()
        for (operation in computer.operations) {
            val operationObject = JsonObject()
            operationObject.addProperty("date", formatter.format(operation.date))
            operationObject.addProperty("version", operation.version)
            operationObject.addProperty("fixes", operation.fixes.joinToString(", "))
            operationsArray.add(operationObject)
        }
        jsonObject.add("operations", operationsArray)
        return jsonObject
    }
}
