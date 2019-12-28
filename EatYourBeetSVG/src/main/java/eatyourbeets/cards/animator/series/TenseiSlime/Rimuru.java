package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.RimuruAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnAfterCardPlayedSubscriber;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rimuru extends AnimatorCard implements OnBattleStartSubscriber, OnAfterCardPlayedSubscriber
{
    public static final String ID = Register(Rimuru.class);

    public AbstractCard copy;

    public Rimuru()
    {
        super(ID, -2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0, 0);

        this.copy = this;

        if (GameUtilities.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetSynergy(Synergies.TenSura, true);
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onAfterCardPlayed.Subscribe(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (card == copy || card instanceof Rimuru || card.purgeOnUse)
        {
            return;
        }

        GameActions.Top.Add(new RimuruAction(this, card));
    }
}