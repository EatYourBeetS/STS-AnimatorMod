package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.utilities.GameActions;

public class Aqua extends AnimatorCard
{
    private boolean transformed = false;

    public static final String ID = Register(Aqua.class);

    public Aqua()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);

        SetHealing(true);
        SetSynergy(Synergies.Konosuba);

        if (InitializingPreview())
        {
            Aqua copy = new Aqua(); // InitializingPreview will be true only once
            copy.SetTransformed(true);
            cardData.InitializePreview(copy, true);
        }
    }

    @Override
    protected void OnUpgrade()
    {
        SetTransformed(transformed);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (upgraded && transformed)
        {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (upgraded && transformed)
        {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!transformed)
        {
            GameActions.Bottom.Heal(magicNumber);
            GameActions.Bottom.Draw(1);
            GameActions.Bottom.Callback(__ -> SetTransformed(true));
        }
        else
        {
            GameActions.Bottom.VFX(new RainbowCardEffect());
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Aqua other = (Aqua) super.makeStatEquivalentCopy();

        other.SetTransformed(transformed);

        return other;
    }

    private void SetTransformed(boolean value)
    {
        transformed = value;

        if (transformed)
        {
            this.loadCardImage(AnimatorResources.GetCardImage(ID + "2"));
            cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0], true);
            transformed = true;
        }
        else
        {
            this.loadCardImage(AnimatorResources.GetCardImage(ID));
            cardText.OverrideDescription(null, true);
            transformed = false;
        }
    }
}