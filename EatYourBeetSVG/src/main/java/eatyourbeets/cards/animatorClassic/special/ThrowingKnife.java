package eatyourbeets.cards.animatorClassic.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.List;

public class ThrowingKnife extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ThrowingKnife.class)
            .SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Ranged)
            .SetColor(CardColor.COLORLESS);
    public static final int INDEX_WEAK = 1;
    public static final int INDEX_VULNERABLE = 2;
    public static final int INDEX_POISON = 3;

    protected Color color;

    public static List<ThrowingKnife> GetAllCards()
    {
        List<ThrowingKnife> result = new ArrayList<>();
        result.add(new ThrowingKnife(INDEX_WEAK));
        result.add(new ThrowingKnife(INDEX_VULNERABLE));
        result.add(new ThrowingKnife(INDEX_POISON));
        return result;
    }

    public static AbstractCard GetRandomCardInBattle()
    {
        RandomizedList<ThrowingKnife> list = CombatStats.GetCombatData(DATA.ID, null);
        if (list == null)
        {
            list = CombatStats.SetCombatData(DATA.ID, new RandomizedList<>());
        }
        if (list.Size() < 2)
        {
            list.AddAll(GetAllCards());
        }

        return list.Retrieve(rng);
    }

    public ThrowingKnife()
    {
        this(0);
    }

    private ThrowingKnife(int index)
    {
        super(DATA);

        this.portraitForeground = new AdvancedTexture(GR.GetTexture(GR.GetCardImage(eatyourbeets.cards.animator.special.ThrowingKnife.DATA.ID + "FG"), true), null);

        Initialize(2, 0, 1, 2);
        SetUpgrade(2, 0);

        SetPurge(true);
        ChangeIndex(index);
    }

    @Override
    public AbstractCard makeCopy()
    {
        return (misc == 0 && GameUtilities.InBattle()) ? GetRandomCardInBattle() : new ThrowingKnife(misc);
    }

    @Override
    public void onMoveToDiscard()
    {
        super.onMoveToDiscard();

        GameActions.Bottom.Callback(() ->
        {
            player.discardPile.removeCard(this);
            freeToPlayOnce = true;
            purgeOnUse = true;
            GameUtilities.PlayManually(this, null);
        });
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && misc == INDEX_WEAK)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m == null || GameUtilities.IsDeadOrEscaped(m))
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.NONE)
                    .SetDamageEffect(this::OnDamage);
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
                    .SetDamageEffect(this::OnDamage);
        }
    }

    protected float OnDamage(AbstractCreature target)
    {
        final AbstractMonster m = JUtils.SafeCast(target, AbstractMonster.class);
        if (m == null || GameUtilities.IsDeadOrEscaped(m))
        {
            return 0;
        }

        if (INDEX_WEAK == misc)
        {
            GameActions.Top.ApplyWeak(player, m, magicNumber);
        }
        else if (INDEX_VULNERABLE == misc)
        {
            GameActions.Top.ApplyVulnerable(player, m, magicNumber);
        }
        else if (INDEX_POISON == misc)
        {
            GameActions.Top.ApplyPoison(player, m, secondaryValue);
        }
        else
        {
            throw new RuntimeException("Invalid index: " + misc);
        }

        return GameEffects.List.Add(VFX.ThrowDagger(m.hb, 0.1f).SetColor(color)).duration * 0.33f;
    }

    private void ChangeIndex(int index)
    {
        this.misc = index;
        this.cardText.OverrideDescription(JUtils.Format(rawDescription, cardData.Strings.EXTENDED_DESCRIPTION[index]), true);

        if (INDEX_WEAK == misc)
        {
            this.color = new Color(0.4f, 0.6f, 0.4f, 1f);
        }
        else if (INDEX_VULNERABLE == misc)
        {
            this.color = new Color(0.8f, 0.2f, 0.2f, 1f);
        }
        else if (INDEX_POISON == misc)
        {
            this.color = new Color(0.2f, 1.0f, 0.2f, 1f);
        }
        else
        {
            this.color = Color.WHITE.cpy();
        }

        this.portraitForeground.color = color;// Colors.Lerp(color, Color.WHITE, 0.35f);
    }
}