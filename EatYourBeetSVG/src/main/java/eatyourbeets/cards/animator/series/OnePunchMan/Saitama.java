package eatyourbeets.cards.animator.series.OnePunchMan;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PummelDamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Saitama extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Saitama.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Saitama(1), false);
        DATA.AddPreview(new Saitama(2), false);
        DATA.AddPreview(new Saitama(3), false);
        DATA.AddPreview(new Saitama(4), false);
        DATA.AddPreview(new Saitama(5), false);
    }

    private int stage;

    private Saitama(int stage)
    {
        super(DATA);

        Initialize(0, 0);

        SetAttackType(EYBAttackType.Normal);
        SetSynergy(Synergies.OnePunchMan);

        GameUtilities.ModifyCostForCombat(this, stage, false);
        this.stage = this.misc = stage;
        SetEffect(stage);
    }

    public Saitama()
    {
        this(0);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        AbstractAttribute damage = super.GetDamageInfo();
        if (damage != null && stage == 4)
        {
            damage.AddMultiplier(magicNumber);
        }

        return damage;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (stage != misc)
        {
            stage = misc = Math.max(Math.min(misc, 5), 0);
            SetEffect(stage);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AbstractCard card = super.makeStatEquivalentCopy();

        Saitama other = JUtils.SafeCast(card, Saitama.class);
        if (other != null)
        {
            other.misc = other.stage = this.misc;
            other.SetEffect(stage);
        }

        return card;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        switch (stage)
        {
            case 0:
            {
                // Do Nothing / Motivate 1
                if (upgraded)
                {
                    GameActions.Bottom.Motivate();
                }

                break;
            }

            case 1:
            {
                // Draw !M! Cards. NL Gain !SV! Agility.
                GameActions.Bottom.Draw(magicNumber);
                GameActions.Bottom.GainAgility(secondaryValue);

                break;
            }

            case 2:
            {
                // Prevent the next time you would lose HP
                GameActions.Bottom.StackPower(new BufferPower(p, 1));

                break;
            }

            case 3:
            {
                // Gain !M! Force. Gain !B! Block.
                GameActions.Bottom.GainForce(magicNumber);
                GameActions.Bottom.GainBlock(block);

                break;
            }

            case 4:
            {
                // Deal !D! damage !M! times.
                for (int i = 1; i < magicNumber; i++)
                {
                    GameActions.Bottom.Add(new PummelDamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                }
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

                break;
            }

            case 5:
            {
                // Remove Intangible. Deal !D! damage. Stun The Enemy
                GameActions.Bottom.RemovePower(p, m, IntangiblePower.POWER_ID);
                GameActions.Bottom.RemovePower(p, m, IntangiblePlayerPower.POWER_ID);
                GameActions.Bottom.RemovePower(p, m, InvinciblePower.POWER_ID);

                GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4f, m.hb.cY - m.hb.height / 4f));
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE).SetPiercing(true, true);
                GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

                GameActions.Bottom.ApplyPower(p, m, new StunMonsterPower(m, 1));

                break;
            }
        }

        GameActions.Bottom.ModifyAllInstances(uuid, c ->
        {
            if (c.misc < 5)
            {
                GameUtilities.ModifyCostForCombat(c, 1, true);
                c.misc += 1;
                c.applyPowers();
            }
        });
    }

    private void SetEffect(int stage)
    {
        switch (stage)
        {
            case 0:
            {
                // Do Nothing
                this.cardText.OverrideDescription(null, true);

                Initialize(0, 0, 0, 0);

                this.target = CardTarget.NONE;
                this.type = CardType.SKILL;

                LoadImage(null);

                break;
            }

            case 1:
            {
                // Draw !M! Cards. NL Gain !SV! Agility.
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);

                Initialize(0, 0, 3, 2);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                LoadImage("_0");

                break;
            }

            case 2:
            {
                // Prevent the next time you would lose HP
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);

                Initialize(0, 0, 0, 0);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                LoadImage("_1");

                break;
            }

            case 3:
            {
                // Gain !M! Force. Gain !B! Block
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[2], true);

                Initialize(0, 9, 6, 0);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                LoadImage("_2");

                break;
            }

            case 4:
            {
                // Deal !D! damage !M! times.
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[3], true);

                Initialize(6, 0, 8, 0);


                this.attackType = EYBAttackType.Normal;
                this.target = CardTarget.ENEMY;
                this.type = CardType.ATTACK;

                LoadImage("_3");

                break;
            }

            case 5:
            {
                // Remove Intangible. Stun the enemy.
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[4], true);

                Initialize(999, 0, 0, 0);


                this.attackType = EYBAttackType.Normal;
                this.target = CardTarget.ENEMY;
                this.type = CardType.ATTACK;

                LoadImage("_4");

                break;
            }
        }
    }
}