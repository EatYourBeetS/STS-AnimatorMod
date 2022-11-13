package eatyourbeets.cards.animatorClassic.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TakanashiRikka extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(TakanashiRikka.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Chuunibyou);

    public TakanashiRikka()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (AbstractCard c : p.hand.getAttacks().group)
        {
            GameActions.Top.MakeCardInHand(c)
            .SetUpgrade(false, true)
            .AddCallback(card ->
            {
                GameUtilities.ModifyCostForCombat(card, 0, false);
                card.freeToPlayOnce = true;
                card.baseDamage = 0;
                card.tags.add(GR.Enums.CardTags.PURGE);
                card.applyPowers();
            });
        }
    }
}