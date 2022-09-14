package tasks

import contributors.*
import kotlinx.coroutines.*


//coroutineScope creates a parent-scope, every async creates child-scope.
//if parent coroutine is ever cancelled, all child coroutines are subsequently be cancelled.
suspend fun loadContributorsConcurrent(service: GitHubServiceSuspend, req: RequestData): List<User> = coroutineScope {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        //If Dispatcher.Default is not specified, the coroutines are created on UI-thread.
        //With Dispatcher.Default, CoroutineDispatcher determines what thread the corresponding coroutine should be run on.
        //async(Dispatchers.Default) {
        async {
            log("starting loading for ${repo.name}")
            delay(5000)
            service.getRepoContributors(req.org, repo.name)
                .also { logUsers(repo, it) }
                .bodyList()
        }
    }
    deferreds.awaitAll().flatten().aggregate()
}
