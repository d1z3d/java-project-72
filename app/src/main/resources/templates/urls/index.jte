@import hexlet.code.dto.url.UrlsPage
@import hexlet.code.util.HtmlUtils
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    @if(!page.getUrls().isEmpty())
        <div class="container-lg mt-5">
            <h1>Сайты</h1>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th>Имя</th>
                        <th class="col-2">Последняя проверка</th>
                        <th class="col-1">Код ответа</th>
                    </tr>
                </thead>
                <tbody>
                @for(var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                        <td>${url.getLastTimeCheck() != null ? HtmlUtils.getTimestampFormatted(url.getLastTimeCheck()) : ""}</td>
                        <td>${!url.getStatusCode().equals(0) ? String.valueOf(url.getStatusCode()) : ""}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    @else
        <p>Список пуст</p>
    @endif
`)
