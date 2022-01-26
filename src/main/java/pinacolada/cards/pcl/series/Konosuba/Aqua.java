package pinacolada.cards.pcl.series.Konosuba;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.orbs.pcl.Water;
import pinacolada.utilities.PCLActions;

public class Aqua extends PCLCard
{
    public static final PCLCardData DATA = Register(Aqua.class)
            .SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Aqua(true), true));

    private boolean transformed = false;

    public Aqua()
    {
        this(false);
    }

    private Aqua(boolean transformed)
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Light(1);
        SetAffinity_Blue(1);

        SetHealing(true);
        SetTransformed(transformed);

        SetAffinityRequirement(PCLAffinity.Blue, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        SetTransformed(transformed);
    }
    
    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return transformed ? null : HPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (upgraded && transformed)
        {
            PCLActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (upgraded && transformed)
        {
            PCLActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!transformed)
        {
            PCLActions.Bottom.Heal(magicNumber);
            PCLActions.Bottom.Draw(1);
            if (upgraded && CheckAffinity(PCLAffinity.Blue) && info.IsSynergizing && info.TryActivateLimited()) {
                PCLActions.Bottom.ChannelOrb(new Water());
            }
            PCLActions.Bottom.Callback(() -> SetTransformed(true));
        }
        else
        {
            PCLActions.Bottom.VFX(new RainbowCardEffect());
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return !upgraded;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Aqua other = (Aqua) super.makeStatEquivalentCopy();

        other.SetTransformed(transformed);

        return other;
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
    public PCLCardPreview GetCardPreview()
    {
        if (transformed)
        {
            return null;
        }

        return super.GetCardPreview();
    }

    private void SetTransformed(boolean value)
    {
        transformed = value;

        if (transformed)
        {
            LoadImage("2");
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0], true);
            type = CardType.STATUS;
        }
        else
        {
            LoadImage(null);
            cardText.OverrideDescription(null, true);
            type = CardType.SKILL;
        }
    }
}