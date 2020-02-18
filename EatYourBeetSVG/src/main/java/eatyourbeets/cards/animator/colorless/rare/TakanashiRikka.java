package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class TakanashiRikka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TakanashiRikka.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public TakanashiRikka()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
        SetSynergy(Synergies.Chuunibyou);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : p.hand.getAttacks().group)
        {
            AbstractCard copy = c.makeStatEquivalentCopy();

            if (copy.cost > 0 || copy.costForTurn > 0)
            {
                copy.cost = 0;
                copy.costForTurn = 0;
                copy.isCostModified = true;
                copy.freeToPlayOnce = true;
            }

            copy.baseDamage = 0;
            copy.tags.add(GR.Enums.CardTags.PURGE);

            GameActions.Top.MakeCardInHand(copy);
        }
    }
}