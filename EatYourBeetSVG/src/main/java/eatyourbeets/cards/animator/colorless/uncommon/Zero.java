package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zero extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zero.class).SetSkill(0, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS);

    public Zero()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetExhaust(true);
        SetSeries(CardSeries.GrimoireOfZero);
        SetAffinity(0, 0, 1, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Top.PlayCard(p.drawPile, m, g -> JUtils.GetRandomElement(g.getSkills().group));
    }
}