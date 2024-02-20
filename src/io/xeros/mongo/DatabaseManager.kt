package io.xeros.mongo

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.mongodb.MongoException
import com.mongodb.client.*
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.query.Query
import org.bson.Document
import org.slf4j.LoggerFactory

object DatabaseManager {


	//var root: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
	var mongodb: Logger = LoggerFactory.getLogger("org.mongodb") as Logger
	var netty: Logger = LoggerFactory.getLogger("io.netty") as Logger
	var reflection: Logger = LoggerFactory.getLogger("org.reflections") as Logger

	init {
		mongodb.level = Level.ERROR
		netty.level = Level.ERROR
		reflection.level = Level.ERROR
	}

    /**
     * The [Environment] used for this connection
     */
	val environment: Environment = CLOUD_ENVIRONMENT

    /**
     * The [MongoClient] used to interface with the database.
     */
	var client: MongoClient? = null

    /**
     * The [MongoDatabase] that the server is connected to.
     */
	var database: MongoDatabase? = null

    /**
     * The morphia [Datastore] where data is stored.
     */
	var datastore: Datastore? = null


	fun initMorphia() {
        datastore = Morphia.createDatastore(client, environment.database)
        datastore!!.ensureIndexes()
		println("Morphia Initialized")
    }

    /**
     * Gets a collection by name from the database.
     */
    fun getCollection(name: String): MongoCollection<Document> {
        return database!!.getCollection(name)
    }

    fun save(toSave : Any) {
		datastore!!.save(toSave)
    }

    fun <K> query(clazz : Class<K>): List<K> {
        return datastore!!.find(clazz).toList()
    }

    fun <K> createQuery(clazz : Class<K>) : Query<K> {
        return datastore!!.find(clazz)
    }

    fun delete(toDelete : Any) : Boolean {
        return datastore?.delete(toDelete)?.wasAcknowledged() ?: false
    }

	fun watch(collection : String): ChangeStreamIterable<Document> {
		return database!!.getCollection(collection).watch()
	}

	fun init() {
		client = MongoClients.create(environment.getAsyncLink())

		if(client == null) {
			throw MongoException("Failed to connect to MongoDB for ${environment.host}:${environment.port}.")
		}

		database = client!!.getDatabase(environment.database)

		System.out.println("Database Connected")

		if(database == null) {
			throw MongoException("Failed to connect to MongoDB database ${environment.database}.")
		}

		initMorphia()

	}

}
