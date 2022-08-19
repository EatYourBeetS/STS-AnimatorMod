package eatyourbeets.cards.animatorClassic.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zero extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Zero.class).SetSkill(0, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS);

    public Zero()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetExhaust(true);
        SetSeries(CardSeries.GrimoireOfZero);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Top.PlayCard(p.drawPile, m, g -> JUtils.Random(g.getSkills().group));
    }
}