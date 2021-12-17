package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.List;

public class ThrowingKnife extends PCLCard
{
    public static final PCLCardData DATA = Register(ThrowingKnife.class)
            .SetAttack(0, CardRarity.SPECIAL, PCLAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetCanGrantAffinity(false);
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

    public static AbstractCard GetRandomCard()
    {
        return new ThrowingKnife(rng.random(1, 3));
    }

    public ThrowingKnife()
    {
        this(0);
    }

    private ThrowingKnife(int index)
    {
        super(DATA);

        this.portraitForeground = new AdvancedTexture(GR.GetTexture(GR.GetCardImage(ThrowingKnife.DATA.ID + "FG"), true), null);

        Initialize(2, 0, 1, 2);
        SetUpgrade(2, 0);

        SetAffinity_Green(1);

        SetPurge(true);
        ChangeIndex(index);
    }

    @Override
    public AbstractCard makeCopy()
    {
        return (misc == 0 && PCLGameUtilities.InBattle()) ? GetRandomCard() : new ThrowingKnife(misc);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Callback(() ->
        {
            player.discardPile.removeCard(this);
            freeToPlayOnce = true;
            purgeOnUse = true;
            PCLGameUtilities.PlayManually(this, null);
        });
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && misc == INDEX_WEAK)
        {
            PCLGameUtilities.GetPCLIntent(m).AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m == null || PCLGameUtilities.IsDeadOrEscaped(m))
        {
            PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.NONE).forEach(d -> d
            .SetDamageEffect(this::OnDamage));
        }
        else
        {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
            .SetDamageEffect(this::OnDamage));
        }
    }

    protected float OnDamage(AbstractCreature target)
    {
        final AbstractMonster m = PCLJUtils.SafeCast(target, AbstractMonster.class);
        if (m == null || PCLGameUtilities.IsDeadOrEscaped(m))
        {
            return 0;
        }

        if (INDEX_WEAK == misc)
        {
            PCLActions.Top.ApplyWeak(player, m, magicNumber);
        }
        else if (INDEX_VULNERABLE == misc)
        {
            PCLActions.Top.ApplyVulnerable(player, m, magicNumber);
        }
        else if (INDEX_POISON == misc)
        {
            PCLActions.Top.ApplyPoison(player, m, secondaryValue);
        }
        else
        {
            throw new RuntimeException("Invalid index: " + misc);
        }

        return PCLGameEffects.List.Add(VFX.ThrowDagger(m.hb, 0.1f).SetColor(color)).duration * 0.33f;
    }

    private void ChangeIndex(int index)
    {
        this.misc = index;
        this.cardText.OverrideDescription(PCLJUtils.Format(rawDescription, cardData.Strings.EXTENDED_DESCRIPTION[index]), true);

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