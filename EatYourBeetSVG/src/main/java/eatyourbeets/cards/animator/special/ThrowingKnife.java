package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class ThrowingKnife extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ThrowingKnife.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS);

    private static ThrowingKnife preview;
    private int index;

    public static ThrowingKnife GetCardForPreview()
    {
        if (preview == null)
        {
            preview = new ThrowingKnife(0);
        }

        return preview;
    }

    public static AbstractCard GetRandomCard()
    {
        return new ThrowingKnife(AbstractDungeon.cardRandomRng.random(1, 3));
    }

    public ThrowingKnife()
    {
        this(0);
    }

    private ThrowingKnife(int index)
    {
        super(DATA);

        Initialize(2, 0, 1, 2);
        SetUpgrade(3, 0);

        SetPurge(true);
        ChangeIndex(index);
    }

    @Override
    public AbstractCard makeCopy()
    {
        if (index == 0 && GameUtilities.InBattle())
        {
            return GetRandomCard();
        }
        else
        {
            return new ThrowingKnife(index);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Callback(__ ->
        {
            AbstractDungeon.player.discardPile.removeCard(this);
            this.freeToPlayOnce = true;
            this.purgeOnUse = true;
            this.applyPowers();
            this.use(AbstractDungeon.player, null);
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m == null || m.isDeadOrEscaped())
        {
            m = GameUtilities.GetRandomEnemy(true);
        }

        if (m != null)
        {
            switch (index)
            {
                case 1:
                    GameActions.Top.ApplyWeak(p, m, magicNumber);
                    break;
                case 2:
                    GameActions.Top.ApplyVulnerable(p, m, magicNumber);
                    break;
                case 3:
                    GameActions.Top.ApplyPoison(p, m, secondaryValue);
                    break;
                default:
                    throw new RuntimeException("This class is only for preview. You are not supposed to use it.");
            }

            GameActions.Top.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE).SetOptions(true, false);

            if (m.hb != null)
            {
                GameActions.Top.VFX(new ThrowDaggerEffect(m.hb.cX, m.hb.cY));
            }
        }
    }

    private void ChangeIndex(int index)
    {
        this.index = index;
        this.cardText.OverrideDescription(JavaUtilities.Format(rawDescription, cardData.Strings.EXTENDED_DESCRIPTION[index]), true);
    }
}