package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.blights.animator.Doomed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Respite extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Respite.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Respite()
    {
        super(DATA);

        Initialize(0, 0, 6 + (GameUtilities.GetAscensionLevel() / 2));
        SetUpgrade(0, 0, magicNumber / 2);

        SetRetain(true);
        SetPurge(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);

        int a = MathUtils.random(1, 3);
        int b = MathUtils.random(1, 3);

        CardCrawlGame.sound.play("SLEEP_" + a + "-" + b);

        AbstractBlight doomed = p.getBlight(Doomed.ID);
        if (doomed != null)
        {
            doomed.setCounter(doomed.counter + 1);
            doomed.updateDescription();
            doomed.flash();
        }
    }
}