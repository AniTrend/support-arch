package co.anitrend.arch.buildSrc.module

internal object Modules {

    interface Module {
        val id: String

        /**
         * @return Formatted id of module as a path string
         */
        fun path(): String = ":$id"
    }

    enum class Support(override val id: String) : Module {
        Core("support-core"),
        Data("support-data"),
        Domain("support-domain"),
        Extensions("support-ext"),
        Recycler("support-recycler"),
        Theme("support-theme"),
        Ui("support-ui")
    }
}