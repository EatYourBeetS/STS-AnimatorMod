package eatyourbeets.cards.animatorClassic.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class HiiragiTenri extends AnimatorClassicCard_UltraRare
{
    public static final EYBCardData DATA = Register(HiiragiTenri.class).SetSkill(4, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public HiiragiTenri()
    {
        super(DATA);

        Initialize(0, 0, 20);
        SetUpgrade(0, 0, 10);

        this.series = CardSeries.OwariNoSeraph;
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MoveCards(player.exhaustPile, player.drawPile).ShowEffect(false, false);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);

        for (AbstractCard c : p.discardPile.group)
        {
            GameActions.Bottom.PlayCard(c, p.discardPile, m).SetExhaust(true);
        }
    }
}