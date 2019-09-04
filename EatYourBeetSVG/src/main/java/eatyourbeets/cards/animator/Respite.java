package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import eatyourbeets.blights.Doomed;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.Hidden;
import eatyourbeets.powers.common.GenericFadingPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import patches.AbstractEnums;

public class Respite extends AnimatorCard implements Hidden
{
    public static final String ID = CreateFullID(Respite.class.getSimpleName());

    public Respite()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0, 0, 6);

        this.tags.add(AbstractEnums.CardTags.PURGE);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        this.retain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainTemporaryHP(p, magicNumber);

        int a = (int)Math.ceil(Math.random() * 3);
        int b = (int)Math.ceil(Math.random() * 3);

        CardCrawlGame.sound.play("SLEEP_" + a + "-" + b);

        AbstractBlight doomed = p.getBlight(Doomed.ID);
        if (doomed != null)
        {
            doomed.setCounter(doomed.counter + 1);
            doomed.updateDescription();
            doomed.flash();
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(3);
        }
    }
}