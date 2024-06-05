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
        Analytics("analytics"),
        Annotation("annotation"),
        Core("core"),
        Data("data"),
        Domain("domain"),
        Extensions("extension"),
        PagingLegacy("paging-legacy"),
        Processor("processor"),
        Recycler("recycler"),
        Request("request"),
        Theme("theme"),
        Ui("ui"),
        RecyclerPagingLegacy("recycler-paging-legacy"),
    }
}