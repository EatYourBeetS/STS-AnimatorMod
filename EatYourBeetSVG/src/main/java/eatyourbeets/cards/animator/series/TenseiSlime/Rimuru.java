package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.RimuruAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rimuru extends AnimatorCard implements OnBattleStartSubscriber, OnAfterCardPlayedSubscriber
{
    public static final EYBCardData DATA = Register(Rimuru.class).SetSkill(-2, CardRarity.RARE, EYBCardTarget.ALL);

    public AbstractCard copy;

    public Rimuru()
    {
        super(DATA);

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
        if (card == copy || card instanceof Rimuru || card.purgeOnUse || card.isInAutoplay)
        {
            return;
        }

        GameActions.Top.Add(new RimuruAction(this, card));
    }
}