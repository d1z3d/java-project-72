@import gg.jte.Content
@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@param Content content
@param BasePage page = null

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <title>SEO page analyzer</title>
</head>
<body class="d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand link-light" href="${NamedRoutes.rootPath()}">Анализатор страниц</a>
        <button class="navbar-toggler collapsed" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav" aria-controls="navbarNavq" aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="navbar-collapse collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link link-secondary" href="${NamedRoutes.rootPath()}">Главная</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link link-secondary" href="${NamedRoutes.urlsPath()}">Сайты</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="flex-grow-1">
    @if(page != null && page.getFlash() != null)
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-info" role="alert">
            <h3>${page.getFlash()}</h3>
        </div>
    @endif
    ${content}
</main>
<footer class="footer border-top py-3 mt-5 bg-light">
    <div class="container-xl">
        <div class="text-center">
            Created by
            <a href="https://github.com/d1z3d" target="_blank">Borychev Andrey</a>
        </div>
    </div>
</footer>
</body>
</html>