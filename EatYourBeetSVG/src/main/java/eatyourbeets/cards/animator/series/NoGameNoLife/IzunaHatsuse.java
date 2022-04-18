package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnLastSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IzunaHatsuse extends AnimatorCard implements OnEndOfTurnLastSubscriber, OnAfterCardPlayedSubscriber
{
    public static final EYBCardData DATA = Register(IzunaHatsuse.class)
            .SetSkill(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new IzunaHatsuse(true), true));

    private boolean transformed;

    public IzunaHatsuse()
    {
        this(false);
    }

    private IzunaHatsuse(boolean transformed)
    {
        super(DATA);

        Initialize(2, 2, 2);
        SetUpgrade(1, 1, 0);

        SetAffinity_Green(1);

        SetTransformed(transformed);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && !transformed)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return transformed ? HPAttribute.Instance.SetCardHeal(this) : null;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return transformed ? null : super.GetBlockInfo();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return transformed ? super.GetDamageInfo().AddMultiplier(2) : null;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        final IzunaHatsuse other = (IzunaHatsuse) super.makeStatEquivalentCopy();

        other.SetTransformed(transformed);

        return other;
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (!transformed && card.uuid != uuid && card.type == CardType.ATTACK)
        {
            CombatStats.onEndOfTurnLast.SubscribeOnce(this);
            SetTransformed(true);
        }
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = transformed ? GameUtilities.GetHealthRecoverAmount(magicNumber) : 0;
    }

    @Override
    public void triggerOnEndOfPlayerTurn()
    {
        super.triggerOnEndOfPlayerTurn();

        SetTransformed(false);
    }

    @Override
    public void OnEndOfTurnLast(boolean isPlayer)
    {
        SetTransformed(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (transformed)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
            GameActions.Bottom.RecoverHP(magicNumber);
            GameActions.Bottom.RetainPower(Affinity.Red);
            GameActions.Bottom.RetainPower(Affinity.Green);
        }
        else
        {
            GameActions.Bottom.GainBlock(block);
            GameActions.Bottom.ApplyWeak(p, m, 1);
            GameActions.Bottom.GainTemporaryStats(-magicNumber, 0, 0);
        }
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {
        if (!transformed)
        {
            super.renderUpgradePreview(sb);
        }
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return transformed ? null : super.GetCardPreview();
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAfterCardPlayed.Subscribe(this);
    }

    private void SetTransformed(boolean value)
    {
        if (transformed != value)
        {
            transformed = value;

            if (transformed)
            {
                LoadImage("Alt");
                SetAttackType(EYBAttackType.Normal);

                affinities.Set(Affinity.Red, 1);
                affinities.Set(Affinity.Dark, 1);

                this.type = CardType.ATTACK;
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            }
            else
            {
                LoadImage(null);

                affinities.Set(Affinity.Red, 0);
                affinities.Set(Affinity.Dark, 0);

                this.type = CardType.SKILL;
                this.cardText.OverrideDescription(null, true);
            }
        }
    }
}