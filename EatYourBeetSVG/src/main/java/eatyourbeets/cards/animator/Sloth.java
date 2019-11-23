package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameUtilities;

public class Sloth extends AnimatorCard
{
    public static final String ID = Register(Sloth.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Sloth()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(40, 0, GetBaseCooldown());

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (PlayerStatistics.getTurnCount() > 0)
        {
            UpdateCooldown(-1);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.GainForce(2);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);

        if (playable)
        {
            int turnCount = PlayerStatistics.getTurnCount();
            if (magicNumber > 0)
            {
                return false;
            }
        }

        return playable;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToDefault(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
        GameActionsHelper.AddToDefault(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
    }

    @Override
    public void upgrade()
    {
        int current = magicNumber;

        if (TryUpgrade())
        {
            if (!CardCrawlGame.isPopupOpen && GameUtilities.InBattle())
            {
                baseMagicNumber = GetBaseCooldown();
                upgradedMagicNumber = true;
                magicNumber = current;

                UpdateCooldown(-1);
            }
            else
            {
                upgradeMagicNumber(-1);
            }
        }
    }

    private void UpdateCooldown(int amount)
    {
        magicNumber += amount;

        if (magicNumber <= 0)
        {
            magicNumber = baseMagicNumber = 0;

            cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);
        }

        baseMagicNumber = magicNumber;
        isMagicNumberModified = false;
    }

    private int GetBaseCooldown()
    {
        return upgraded ? 3 : 4;
    }
}